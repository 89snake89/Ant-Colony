package antcolony;

import java.util.*;



/**
 * Basic implementation of K-means clustering.  Since it's a Runnable, it's 
 * designed to be executed by a dedicated thread, but that thread
 * does not create any other threads to divide up the work.
 * 
 * @version     1.0
 * @since       1.0
 */
public class KMeans {

    // Temporary clusters used during the clustering process.  Converted to
    // an array of the simpler class Cluster at the conclusion.
    private ProtoCluster[] mProtoClusters;

    // Cache of coordinate-to-cluster distances. Number of entries = 
    // number of clusters X number of coordinates.
    private double[][] mDistanceCache;

    // Used in makeAssignments() to figure out how many moves are made
    // during each iteration -- the cluster assignment for coordinate n is
    // found in mClusterAssignments[n] where the N coordinates are numbered
    // 0 ... (N-1)
    private int[] mClusterAssignments;

    // List of items to be clustered
    private HashMap<UUID,Item> items;
    
    // an auxiliary dictionary of item ids
    private UUID[] dictionary;
    
    // 2D array holding the coordinates to be clustered.
    private double[][] mCoordinates;
    // The desired number of clusters and maximum number
    // of iterations.
    private int mK, mMaxIterations;
    // Seed for the random number generator used to select
    // coordinates for the initial cluster centers.
    private long mRandomSeed;
    
    // An array of Cluster objects: the output of k-means.
    private Cluster[] mClusters;
    
    // The type of seed centering
    private int type;
    
    // The sizes of the grid
    private int xsize, ysize;
    
    // A list of the centers
    private LinkedList<double[]> centers;
    
    // A list of the seed centers
    private LinkedList<UUID> init_centers;
  
    /**
     * Constructor
     * 
     * @param conf The configuration of the current running simulation
     * @param it the list of items to be clustered
     * @param k  the number of desired clusters.
     * @param list the list of initial centers
     * @param maxIterations the maximum number of clustering iterations.
     * @param randomSeed seed used with the random number generator.
     * @param t the type of initial centers, 0 - random centers, 1 - k-means++ , 2 - given in the list
     */
    public KMeans(Configuration conf, HashMap<UUID,Item> it, int k, LinkedList<UUID> list, int maxIterations, long randomSeed, int t) {

    	this.xsize = conf.getxsize();
    	this.ysize = conf.getysize();
    	mCoordinates = new double[it.size()][3];
    	dictionary = new UUID[it.size()];
    	items = it;
    	type = t;
        int i = 0;
        for (Map.Entry<UUID, Item> entry : it.entrySet()) {
        	mCoordinates[i][0] = entry.getValue().getX();
        	mCoordinates[i][1] = entry.getValue().getY();
        	dictionary[i] = entry.getValue().getID();
        	i++;
        }
        init_centers = list;
        if (k==0) mK = list.size();
        else mK = Math.min(k, mCoordinates.length);
        mMaxIterations = maxIterations;
        mRandomSeed = randomSeed;
    }

    /**
     * Get the clusters computed by the algorithm.  This method should
     * not be called until clustering has completed successfully.
     * 
     * @return an array of Cluster objects.
     */
    public Cluster[] getClusters() {
        return mClusters;
    }
    
    /**
     * Get the current centers of the algorithm
     * 
     * @return a List with centers.
     */
    public LinkedList<double[]> getCenters() {
        return centers;
    }
    
    /**
     * Run the clustering algorithm.
     */
    public void compute() {

        try {
            
            initCenters();
            computeDistances();
            makeAssignments();

            // Number of moves in the iteration and the iteration counter.
            int moves = 0, it = 0;
            
            do {
                computeCenters();
                computeDistances();
                moves = makeAssignments();
                it++;
            } while (moves > 0 && it < mMaxIterations);

            mClusters = generateFinalClusters();
            
        } catch (Throwable t) {
            System.out.println(t);
            t.printStackTrace();
            
        } finally {cleanup();}
    }

    /**
     * Randomly select coordinates to be the initial cluster centers.
     */
    private void initCenters() {

        Random random = new Random(mRandomSeed);     
        int coordCount = mCoordinates.length;
        if (mClusterAssignments == null) {
            mClusterAssignments = new int[coordCount];
            Arrays.fill(mClusterAssignments, -1);
        }

        // Place the coordinate indices into an array and shuffle it.
        int[] indices = new int[coordCount];
        for (int i = 0; i < coordCount; i++) {
            indices[i] = i;
        }
        for (int i = 0, m = coordCount; m > 0; i++, m--) {
            int j = i + random.nextInt(m);
            if (i != j) {
                // Swap the indices.
                indices[i] ^= indices[j];
                indices[j] ^= indices[i];
                indices[i] ^= indices[j];
            }
        }

        mProtoClusters = new ProtoCluster[mK];
        ArrayList<Integer> ind = new ArrayList<Integer>();
        for (int i=0; i<mK; i++) {
            int coordIndex = indices[i];
            switch (type){
            case 0 : 		mProtoClusters[i] = new ProtoCluster(mCoordinates[coordIndex], coordIndex);
            				mClusterAssignments[indices[i]] = i;
            			    break;
            case 1 : 		if (i==0){
            					mProtoClusters[i] = new ProtoCluster(mCoordinates[coordIndex], coordIndex);
            					mClusterAssignments[indices[i]] = i;
            					}
            				else {
            					double maxDist = 0;
            					int max = coordIndex;
            					for (int j=0; j< coordCount; j++){
            						if (ind.contains(j)) continue;
            							for (int l=0; l<i; l++){
            							double d = distance(mCoordinates[j],mProtoClusters[l].mCenter,this.xsize,this.ysize);
            						if (d > maxDist) {
            							maxDist= d;
            							max = j;
            						}
                				mProtoClusters[i] = new ProtoCluster(mCoordinates[max], max);
                				mClusterAssignments[max] = i;
                				ind.add(max);
            					}}
            				}
            case 2:			int l=0;
            				while (!dictionary[l].equals(init_centers.get(i))) l++;
            				mProtoClusters[i] = new ProtoCluster(mCoordinates[l], l);
        					mClusterAssignments[indices[l]] = i;
							break;
           }
        }
    }

    /**
     * Recompute the centers of the protoclusters with 
     * update flags set to true.
     */
    private void computeCenters() {
        
        int numClusters = mProtoClusters.length;
        
        // Sets the update flags of the protoclusters that haven't been deleted and
        // whose memberships have changed in the iteration just completed.
        //
        for (int c = 0; c < numClusters; c++) {
            ProtoCluster cluster = mProtoClusters[c];
            if (cluster.getConsiderForAssignment()) {
                if (!cluster.isEmpty()) {
                    // This sets the protocluster's update flag to
                    // true only if its membership changed in last call
                    // to makeAssignments().  
                    cluster.setUpdateFlag();
                    // If the update flag was set, update the center.
                    if (cluster.needsUpdate()) {
                        cluster.updateCenter(mCoordinates);
                    }
                } else {
                    // When a cluster loses all of its members, it
                    // falls out of contention.  So it is possible for
                    // k-means to return fewer than k clusters.
                    cluster.setConsiderForAssignment(false);
                }
            }
        }
    }

    
    /** 
     * Compute distances between coodinates and cluster centers,
     * storing them in the distance cache.  Only distances that
     * need to be computed are computed.  This is determined by
     * distance update flags in the protocluster objects.
     */
    private void computeDistances() {
        
        int numCoords = mCoordinates.length;
        int numClusters = mProtoClusters.length;

        if (mDistanceCache == null) mDistanceCache = new double[numCoords][numClusters];
        

        for (int coord=0; coord < numCoords; coord++) {
            // Update the distances between the coordinate and all
            // clusters currently in contention with update flags set.
            for (int clust=0; clust<numClusters; clust++) {
                ProtoCluster cluster = mProtoClusters[clust];
                if (cluster.getConsiderForAssignment() && cluster.needsUpdate()) {
                    mDistanceCache[coord][clust] = 
                        distance(mCoordinates[coord], cluster.getCenter(), this.xsize, this.ysize);
                }
            }
        }
        
    }
    
    /** 
     * Assign each coordinate to the nearest cluster.  Called once
     * per iteration.  Returns the number of coordinates that have
     * changed their cluster membership.
     */
    private int makeAssignments() {

        int moves = 0;
        int coordCount = mCoordinates.length;

        // Checkpoint the clusters, so we'll be able to tell
        // which ones have changed after all the assignments have been
        // made.
        int numClusters = mProtoClusters.length;
        for (int c = 0; c < numClusters; c++) {
            if (mProtoClusters[c].getConsiderForAssignment()) {
                mProtoClusters[c].checkPoint();
            }
        }

        // Now do the assignments.
        for (int i = 0; i < coordCount; i++) {
            int c = nearestCluster(i);
            mProtoClusters[c].add(i);
            if (mClusterAssignments[i] != c) {
                mClusterAssignments[i] = c;
                moves++;
            }
        }

        return moves;
    }

    /**
     * Find the nearest cluster to the coordinate identified by
     * the specified index.
     */
    private int nearestCluster(int ndx) {
        int nearest = -1;
        double min = Double.MAX_VALUE;
        int numClusters = mProtoClusters.length;
        for (int c = 0; c < numClusters; c++) {
            if (mProtoClusters[c].getConsiderForAssignment()) {
                double d = mDistanceCache[ndx][c];
                if (d < min) {
                    min = d;
                    nearest = c;
                }
            }
        }
        return nearest;
    }
 
    /**
     * Compute the euclidean distance between the two arguments.
     */
    private static double distance(double[] coord, double[] center, int xsize, int ysize) {
    	double xdiff = Math.abs(center[0] - coord[0]);
		xdiff = Math.min(xdiff, xsize-xdiff);
		double ydiff = Math.abs(center[1] - coord[1]);
		ydiff = Math.min(ydiff, ysize-ydiff);
		return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
    }

    /**
     * Generate an array of Cluster objects from mProtoClusters.
     * 
     * @return array of Cluster object references.
     */
    private Cluster[] generateFinalClusters() {
        
        int numClusters = mProtoClusters.length;
        
        this.centers = new LinkedList<double[]>();
        
        // Convert the proto-clusters to the final Clusters.
        //
        // - accumulate in a list.
        List<Cluster> clusterList = new ArrayList<Cluster>(numClusters);
        for (int c = 0; c < numClusters; c++) {
            ProtoCluster pcluster = mProtoClusters[c];
            if (!pcluster.isEmpty()) {
            	int[] members = pcluster.getMembership();
            	centers.add(pcluster.mCenter);
            	Item[] list = new Item[members.length];
            	int i=0;
            	for (int k : members) {
            		list[i]=items.get(dictionary[k]);
            		i++;
            		}
                Cluster cluster = new Cluster(list, pcluster.getCenter(), c);
                clusterList.add(cluster);
            }
        }
    
        // - convert list to an array.
        Cluster[] clusters = new Cluster[clusterList.size()];
        clusterList.toArray(clusters);

        return clusters;
    }
    
    /**
     * Clean up items used by the clustering algorithm that are no longer needed.
     */
    private void cleanup() {
        mProtoClusters = null;
        mDistanceCache = null;
        mClusterAssignments = null;
    }

    /**
     * Cluster class used temporarily during clustering.  Upon completion,
     * the array of ProtoClusters is transformed into an array of
     * Clusters.
     */
    private static class ProtoCluster {

        // The previous iteration's cluster membership and
        // the current iteration's membership.  Compared to see if the
        // cluster has changed during the last iteration.
        private int[] mPreviousMembership;
        private int[] mCurrentMembership;
        private int mCurrentSize;

        // The cluster center.
        private double[] mCenter;

        // Born true, so the first call to updateDistances() will set all the
        // distances.
        private boolean mUpdateFlag = true;
        // Whether or not this cluster takes part in the operations.
        private boolean mConsiderForAssignment = true;

        /**
         * Constructor
         * 
         * @param center  the initial cluster center.
         * @param coordIndex  the initial member. 
         */
        ProtoCluster(double[] center, int coordIndex) {
            mCenter = (double[]) center.clone();
            // No previous membership.
            mPreviousMembership = new int[0];
            // Provide space for 10 members to be added initially.
            mCurrentMembership = new int[10];
            mCurrentSize = 0;
            add(coordIndex);
        }

        /**
         * Get the members of this protocluster.
         * 
         * @return an array of coordinate indices.
         */
        int[] getMembership() {
            trimCurrentMembership();
            return mCurrentMembership;
        }
        
        /**
         * Get the protocluster's center.
         * 
         * @return
         */
        double[] getCenter() {
            return mCenter;
        }
        
        /**
         * Reduces the length of the array of current members to
         * the number of members.
         */
        void trimCurrentMembership() {
            if (mCurrentMembership.length > mCurrentSize) {
                int[] temp = new int[mCurrentSize];
                System.arraycopy(mCurrentMembership, 0, temp, 0, mCurrentSize);
                mCurrentMembership = temp;
            }
        }

        /**
         * Add a coordinate to the protocluster.
         * 
         * @param ndx index of the coordinate to be added.
         */
        void add(int ndx) {
            // Ensure there's space to add the new member.
            if (mCurrentSize == mCurrentMembership.length) {
                // If not, double the size of mCurrentMembership.
                int newCapacity = Math.max(10, 2*mCurrentMembership.length);
                int[] temp = new int[newCapacity];
                System.arraycopy(mCurrentMembership, 0, temp, 0, mCurrentSize);
                mCurrentMembership = temp;
            }
            // Add the index.
            mCurrentMembership[mCurrentSize++] = ndx;
        }

        /**
         * Does the protocluster contain any members?
         * 
         * @return true if the cluster is empty.
         */
        boolean isEmpty() {
            return mCurrentSize == 0;
        }

        /**
         * Compares the previous and the current membership.
         * Sets the update flag to true if the membership
         * changed in the previous call to makeAssignments().
         */
        void setUpdateFlag() {
            // Trim the current membership array length down to the
            // number of members.
            trimCurrentMembership();
            mUpdateFlag = false;
            if (mPreviousMembership.length == mCurrentSize) {
                for (int i=0; i<mCurrentSize; i++) {
                    if (mPreviousMembership[i] != mCurrentMembership[i]) {
                        mUpdateFlag = true;
                        break;
                    }
                }
            } else { // Number of members has changed.
                mUpdateFlag = true;
            }
        }

        /**
         * Clears the current membership after copying it to the
         * previous membership.
         */
        void checkPoint() {
            mPreviousMembership = mCurrentMembership;
            mCurrentMembership = new int[10];
            mCurrentSize = 0;
        }

        /**
         * Is this protocluster currently in contention?
         * 
         * @return true if this cluster is still in the running.
         */
        boolean getConsiderForAssignment() {
            return mConsiderForAssignment;
        }

        /**
         * Set the flag to indicate that this protocluster is
         * in or out of contention.
         * 
         * @param b
         */
        void setConsiderForAssignment(boolean b) {
            mConsiderForAssignment = b;
        }

        /**
         * Get the value of the update flag.  This value is
         * used to determine whether to update the cluster center and
         * whether to recompute distances to the cluster.
         * 
         * @return the value of the update flag.
         */
        boolean needsUpdate() {
            return mUpdateFlag;
        }

        /**
         * Update the cluster center.
         * 
         * @param coordinates the array of coordinates.
         */
        void updateCenter(double[][] coordinates) {
            Arrays.fill(mCenter, 0.0);
            if (mCurrentSize > 0) {
                for (int i=0; i<mCurrentSize; i++) {
                    double[] coord = coordinates[mCurrentMembership[i]];
                    for (int j=0; j<coord.length; j++) {
                        mCenter[j] += coord[j];
                    }
                }
                for (int i=0; i<mCenter.length; i++) {
                    mCenter[i] /= mCurrentSize;
                }
            }
        }
    }

}
