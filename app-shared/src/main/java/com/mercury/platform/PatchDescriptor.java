package com.mercury.platform;

import com.mercury.platform.files.FileDescriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class PatchDescriptor<E extends FileDescriptor> implements Iterable<E>, Serializable{
    private List<E> descriptors = new ArrayList<>();
    public void add(E object){
        descriptors.add(object);
    }
    @Override
    public Iterator<E> iterator() {
        return descriptors.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        descriptors.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return descriptors.spliterator();
    }
}
