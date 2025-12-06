package core;
import core.interfaces.Controllable;
import core.interfaces.EnergyConsumer;
import core.interfaces.Schedulable;
import events.Event;
import events.EventListener;

public abstract class SmartDevice     implements Controllable, EnergyConsumer, Schedulable, EventListener{
protected String deviceId;
protected String deviceName;
   protected boolean isPoweredOn;
public SmartDevice(String deviceId,String deviceName){
    this.deviceId=deviceId;
    this.deviceName=deviceName;
    isPoweredOn=false;
}
@Override
public void turnOn(){isPoweredOn=true;
 System.out.println("[SmartDevice] " + deviceName + " turned ON.");
}
public void turnOff(){isPoweredOn=false;
 System.out.println("[SmartDevice] " + deviceName + " turned OFF.");
 
}
  public boolean isOn() {
        return this.isPoweredOn;
    }
        @Override
    public abstract double calculatePowerUsage();
      @Override
    public abstract void executeScheduledTask();
        @Override  // From EventListener
    public abstract void onEvent(Event event);
    //additional (not from interfaces) 
   public abstract void performAction();
   //Getters
   public String getDeviceId(){return deviceId;}
      public String getDeviceName(){return deviceName;}}


