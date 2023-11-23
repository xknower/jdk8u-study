package com.sun.tools.example.debug.gui;

import javax.swing.tree.*;

public class SingleLeafTreeSelectionModel extends DefaultTreeSelectionModel {

    private static final long serialVersionUID = -7849105107888117679L;

    SingleLeafTreeSelectionModel() {
        super();
        selectionMode = SINGLE_TREE_SELECTION;
    }

    @Override
    public void setSelectionPath(TreePath path) {
        if(((TreeNode)(path.getLastPathComponent())).isLeaf()) {
            super.setSelectionPath(path);
        }
    }

    @Override
    public void setSelectionPaths(TreePath[] paths) {
        // Only look at first path, as all others will be
        // ignored anyway in single tree selection mode.
        if(((TreeNode)(paths[0].getLastPathComponent())).isLeaf()) {
            super.setSelectionPaths(paths);
        }
    }

    @Override
    public void addSelectionPath(TreePath path) {
        if(((TreeNode)(path.getLastPathComponent())).isLeaf()) {
            super.setSelectionPath(path);
        }
    }

    @Override
    public void addSelectionPaths(TreePath[] paths) {
        // Only look at first path, as all others will be
        // ignored anyway in single tree selection mode.
        if(((TreeNode)(paths[0].getLastPathComponent())).isLeaf()) {
            super.addSelectionPaths(paths);
        }
    }
}
