package me.cookie.nio.workflow;

import me.cookie.nio.group.Group;
import me.cookie.nio.worker.Worker;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cookie on 2017/6/28.
 */
public abstract class GroupWorkflow implements Workflow{

    private List<WorkflowGroupNode> groupList = new LinkedList<>();

    @Override
    public void addLast(Group group){
        WorkflowGroupNode lastNode = null;
        if(groupList.size()!=0){
            lastNode = groupList.get(groupList.size()-1);
        }
        WorkflowGroupNode newNode = new WorkflowGroupNode(group,null);
        groupList.add(newNode);
        if(lastNode != null){
            lastNode.setNext(newNode);
        }
    }

    List<WorkflowGroupNode> getGroupList(){
        return groupList;
    }

    @Override
    public Group next(Group target){
        if(target instanceof WorkflowGroupNode){
            return ((WorkflowGroupNode)target).getNext();
        }
        WorkflowGroupNode next = groupList.stream().filter(node -> node.getCurrent() == target).findFirst().orElse(null);
        return next == null ? null : next.getNext();
    }


    private static class WorkflowGroupNode implements Group {
        private Group current;
        private WorkflowGroupNode next;
        public WorkflowGroupNode(Group current,WorkflowGroupNode next){
            this.current = current;
            this.next = next;
        }

        public void setNext(WorkflowGroupNode next){
            this.next = next;
        }

        public WorkflowGroupNode getNext(){
            return next;
        }

        public Group getCurrent(){
            return current;
        }

        @Override
        public void init() {
            current.init();
        }

        @Override
        public boolean isInit() {
            return current.isInit();
        }

        @Override
        public void start() {
            current.start();
        }

        @Override
        public boolean isStart() {
            return current.isStart();
        }

        @Override
        public void finish() {
            current.finish();
        }

        @Override
        public boolean isFinish() {
            return current.isFinish();
        }

        @Override
        public void accept(Object obj) {
            current.accept(obj);
        }

        @Override
        public Collection<Worker> getWorkers() {
            return current.getWorkers();
        }
    }
}
