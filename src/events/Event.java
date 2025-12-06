package events;  

public class Event {
   
    private String type;       
    private String source;      
    private long timestamp;    
    private String extraData;   
    
    // First constructor for normal events (e.g., motion in hallway)
    public Event(String type, String source) {
        this.type = type;
        this.source = source;
        this.extraData = "";
        this.timestamp = System.currentTimeMillis();
    }
    
    // Second constructor for events with extra data (e.g., smoke with severity)
    public Event(String type, String source, String extraData) {
        this.type = type;
        this.source = source;
        this.extraData = extraData;  
        this.timestamp = System.currentTimeMillis();
    }
    
    //getters
    public String getSource() { 
        return source;  
    }
    
    public String getType() { 
        return type;    
    }
    
    public String getExtraData() {  
        return extraData;
    }
    
    public long getTimestamp() {
        return timestamp; 
    }
    
    // setter
    public void setExtraData(String extraData) {  
        this.extraData = extraData;
    }
    
    // Optional:
    @Override
    public String toString() {
        return "Event[type=" + type + ", source=" + source + 
               ", extraData=" + extraData + "]";
    }
}