import java.util.ArrayList;

class Task {
    private String taskId;
    private ArrayList<String> items;

    public Task(String taskId, ArrayList<String> items) {
        this.taskId = taskId;
        this.items = items;
    }

//    public String getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }

    public ArrayList<String> getItems() {
        return items;
    }

//    public void setItems(ArrayList<String> items) {
//        this.items = items;
//    }
}