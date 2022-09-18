package org.xxpay.core.common.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PermTreeBuilder {

    List<Node> nodes = new ArrayList<>();

    public static String buildTree(List<Node> nodes) {

        PermTreeBuilder treeBuilder = new PermTreeBuilder(nodes);

        return treeBuilder.buildJSONTree();
    }

    public static List<Node> buildListTree(List<Node> nodes) {

        PermTreeBuilder treeBuilder = new PermTreeBuilder(nodes);

        return treeBuilder.buildTree();
    }

    public PermTreeBuilder(List<Node> nodes) {
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
        List<Node> treeNodes = new ArrayList<>();
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
            node.setData(children);
        }
    }

    // 获取父节点下所有的子节点
    public List<Node> getChildNodes(Node pnode) {
        List<Node> childNodes = new ArrayList<>();
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
        List<Node> rootNodes = new ArrayList<>();
        for (Node n : nodes) {
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }

    public static class Node {
        private Long resourceId;
        private String title;
        private String value;
        private Boolean checked = false;
        private Boolean disabled = false;
        private Long parentId;
        private List<Node> data = new ArrayList<>();

        public Long getResourceId() {
            return resourceId;
        }

        public void setResourceId(Long resourceId) {
            this.resourceId = resourceId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public Boolean getDisabled() {
            return disabled;
        }

        public void setDisabled(Boolean disabled) {
            this.disabled = disabled;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public List<Node> getData() {
            return data;
        }

        public void setData(List<Node> data) {
            this.data = data;
        }
    }
}