package com.mercury.platform.ui.adr.components.panel.tree.model;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class AdrTreeNode<T extends AdrComponentDescriptor> implements Iterable<AdrTreeNode<T>> {
    @Getter
    private JPanel panel;
    @Getter
    private T data;
    @Getter
    private AdrTreeNode<T> parent;
    private List<AdrTreeNode<T>> children;

    public AdrTreeNode(T data, JPanel panel) {
        this.data = data;
        this.panel = panel;
        this.children = new ArrayList<>();
    }

    public void setPanel(JPanel panel) {
        if (this.parent.getPanel() != null) {
            parent.getPanel().remove(this.panel);
            this.panel = panel;
            parent.getPanel().add(panel);
        }
    }

    public AdrTreeNode<T> addChild(T data, JPanel panel) {
        AdrTreeNode<T> childNode = new AdrTreeNode<>(data, panel);
        childNode.parent = this;
        this.panel.add(panel);
        this.children.add(childNode);
        return childNode;
    }

    public AdrTreeNode<T> addChild(T data) {
        AdrTreeNode<T> childNode = new AdrTreeNode<>(data, panel);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public void removeChild(T data) {
        new ArrayList<>(this.children).forEach(it -> {
            if (it.getData().equals(data)) {
                if (this.data != null && this.data.getType().equals(AdrComponentType.TRACKER_GROUP)) {
                    ((AdrTrackerGroupDescriptor) this.data).getCells().remove(data);
                }
                this.panel.remove(it.getPanel());
                this.children.remove(it);
            }
        });
    }

    public void duplicateChild(AdrComponentDescriptor descriptor) {
        this.forEach(it -> {
            if (it.getData().equals(descriptor)) {
                try {
                    AdrTreeNode<T> clone = (AdrTreeNode<T>) it.clone();
                    this.children.add(clone);
                    this.panel.add(clone.getPanel());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            } else {
                it.duplicateChild(descriptor);
            }
        });
    }

    public boolean contains(T data) {
        for (AdrTreeNode<T> child : this.children) {
            if (child.getData().equals(data)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<AdrTreeNode<T>> iterator() {
        return this.children.iterator();
    }

    @Override
    public void forEach(Consumer<? super AdrTreeNode<T>> action) {
        new ArrayList<>(this.children).forEach(action::accept);
    }
}
