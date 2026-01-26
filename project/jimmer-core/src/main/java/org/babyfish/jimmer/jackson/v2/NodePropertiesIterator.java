package org.babyfish.jimmer.jackson.v2;

import com.fasterxml.jackson.databind.JsonNode;
import org.babyfish.jimmer.jackson.codec.Node;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

public class NodePropertiesIterator implements Iterator<Map.Entry<String, Node>> {
    private final Iterator<Map.Entry<String, JsonNode>> nodeIterator;

    public NodePropertiesIterator(Iterator<Map.Entry<String, JsonNode>> nodeIterator) {
        this.nodeIterator = nodeIterator;
    }

    @Override
    public boolean hasNext() {
        return nodeIterator.hasNext();
    }

    @Override
    public void remove() {
        nodeIterator.remove();
    }

    @Override
    public Map.Entry<String, Node> next() {
        Map.Entry<String, JsonNode> entry = nodeIterator.next();
        return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), new NodeImpl(entry.getValue()));
    }
}
