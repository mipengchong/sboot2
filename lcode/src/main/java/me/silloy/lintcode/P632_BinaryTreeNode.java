package me.silloy.lintcode;

/**
 * 描述
 * 在二叉树中寻找值最大的节点并返回。
 * <p>
 * 您在真实的面试中是否遇到过这个题？
 * 样例
 * 给出如下一棵二叉树：
 * <p>
 * 1
 * /   \
 * -5     2
 * / \   /  \
 * 0   3 -4  -5
 * 返回值为 3 的节点。
 */
public class P632_BinaryTreeNode {
    /*
     * @param root: the root of tree
     * @return: the max node
     */
    public TreeNode maxNode(TreeNode root) {
        // Write your code here
        if(root == null) {
            return null;
        }
        TreeNode left = root;
        TreeNode right = root;
        if(root.left != null) {
            left = maxNode(root.left);
        }
        if(root.right != null) {
            right = maxNode(root.right);
        }
        if(left.val > root.val) {
            root.val = left.val;
        }
        if(right.val > root.val) {
            root.val = right.val;
        }
        return root;
    }


    public class TreeNode {
        public int val;
        public TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }
}
