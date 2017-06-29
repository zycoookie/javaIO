package me.cookie.nio.workflow;

import lombok.Getter;
import lombok.Setter;
import me.cookie.nio.Context;

/**
 * Created by cookie on 2017/6/28.
 */
@Getter
@Setter
public class ServerContext extends Context {
    private Workflow workflow;
}
