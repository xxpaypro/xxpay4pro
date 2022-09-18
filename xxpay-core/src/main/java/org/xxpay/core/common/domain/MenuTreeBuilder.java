package org.xxpay.core.common.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MenuTreeBuilder {

    List<Node> nodes = new ArrayList<Node>();

    public static String buildTree(List<Node> nodes) {

        MenuTreeBuilder treeBuilder = new MenuTreeBuilder(nodes);

        return treeBuilder.buildJSONTree();
    }

    public MenuTreeBuilder(List<Node> nodes) {
        super();
        this.nodes = nodes;
    }

    // 构建JSON树形结构
    public String buildJSONTree() {
        List<Node> nodeTree = buildTree();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(nodeTree));
        return jsonArray.toString();
    }

    // 构建树形结构
    public List<Node> buildTree() {
        List<Node> treeNodes = new ArrayList<Node>();
        List<Node> rootNodes = getRootNodes();
        for (Node rootNode : rootNodes) {
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }

    // 递归子节点
    public void buildChildNodes(Node node) {
        List<Node> children = getChildNodes(node);
        if (!children.isEmpty()) {
            for (Node child : children) {
                buildChildNodes(child);
            }
            node.setList(children);
        }
    }

    // 获取父节点下所有的子节点
    public List<Node> getChildNodes(Node pnode) {
        List<Node> childNodes = new ArrayList<Node>();
        for (Node n : nodes) {
            if (pnode.getResourceId().equals(n.getParentId())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    // 判断是否为根节点
    public boolean rootNode(Node node) {
        boolean isRootNode = true;
        for (Node n : nodes) {
            if (node.getParentId() != null && node.getParentId().equals(n.getResourceId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    // 获取集合中所有的根节点
    public List<Node> getRootNodes() {
        List<Node> rootNodes = new ArrayList<Node>();
        for (Node n : nodes) {
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }

    public static class Node {
        private Long resourceId;
        private String name;
        private String title;
        private String jump;
        private String icon;
        private Long parentId;
        private List<Node> list;

        public Long getResourceId() {
            return resourceId;
        }

        public void setResourceId(Long resourceId) {
            this.resourceId = resourceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getJump() {
            return jump;
        }

        public void setJump(String jump) {
            this.jump = jump;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public List<Node> getList() {
            return list;
        }

        public void setList(List<Node> list) {
            this.list = list;
        }
    }
}