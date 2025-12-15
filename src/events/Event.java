package events;  

public class Event {
   
    private String type;       
    private String source;      
    private long timestamp;    
    private String extraData;   
    private String message;
    
    // fixed person 5 : merged both constructors in one 
    public Event(String type, String source, String message, String extraData) {
        this.type = type;
        this.source = source;
        this.message = message != null ? message : "";
        this.extraData = extraData != null ? extraData : "";
        this.timestamp = System.currentTimeMillis();
    }
    
    //getters
    public String getMessage() {
        return message;
    }
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