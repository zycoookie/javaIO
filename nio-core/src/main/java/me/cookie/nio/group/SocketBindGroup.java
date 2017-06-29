package me.cookie.nio.group;

import me.cookie.nio.worker.WorkerFactory;

import java.net.SocketAddress;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by cookie on 2017/6/25.
 */
public abstract class SocketBindGroup extends AbstractGroup {

    protected Set<SocketAddress> socketBindList = new LinkedHashSet<>();

    SocketBindGroup(WorkerFactory factory) {
        super(factory);
    }

    public void bind(SocketAddress address){
        socketBindList.add(address);
        doBind(address);
    }

    public void unBind(SocketAddress address){
        socketBindList.remove(address);
        doUnbind(address);
    }

    protected abstract void doUnbind(SocketAddress address);

    public abstract void doBind(SocketAddress address);

    @Override
    public void accept(Object obj) {
        if(obj instanceof SocketAddress){
            bind((SocketAddress) obj);
        }
    }
}

