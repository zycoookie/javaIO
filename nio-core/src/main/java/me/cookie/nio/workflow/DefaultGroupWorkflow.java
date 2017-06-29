package me.cookie.nio.workflow;

import lombok.extern.slf4j.Slf4j;
import me.cookie.nio.arch.Life;
import me.cookie.nio.group.Group;

/**
 * Created by cookie on 2017/6/28.
 */
@Slf4j
public class DefaultGroupWorkflow extends GroupWorkflow {

    private ServerContext context;

    private Life life = new Life();

    public DefaultGroupWorkflow(ServerContext serverContext,Group... group){
        this.context = serverContext;
        batchAddGroups(group);
    }

    private void batchAddGroups(Group... groups){
        if(groups != null){
            for(Group g : groups){
                addLast(g);
            }
        }
    }

    @Override
    public void init() {
        log.info("init all group");
        for(Group group : getGroupList()){
            group.init();
        }
        life.init();
    }

    @Override
    public boolean isInit() {
        return life.isInit();
    }

    @Override
    public void start() {
        life.start();
    }

    @Override
    public boolean isStart() {
        return life.isStart();
    }

    @Override
    public void finish() {
        life.finish();
    }

    @Override
    public boolean isFinish() {
        return life.isFinish();
    }

    @Override
    public ServerContext getSeverContext() {
        return context;
    }
}
