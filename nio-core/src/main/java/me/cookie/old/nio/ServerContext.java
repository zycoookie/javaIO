package me.cookie.old.nio;

import lombok.Getter;
import lombok.Setter;
import me.cookie.old.nio.request.Job;

import java.nio.channels.Selector;
import java.util.Set;

/**
 * Created by cookie on 2017/6/20.
 */
@Getter
@Setter
public class ServerContext {
    private Group workGroup;
    private Group eventGroup;
    private Group ServerGroup;
    private Job eventListener;
    private Set<Selector> serverSelector;
    private Set<Selector> eventSelector;
}
