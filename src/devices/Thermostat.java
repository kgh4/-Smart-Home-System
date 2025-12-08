package devices;

import core.SmartDevice;
import enums.HeatingMode;
import events.Event;
import java.util.ArrayList;
import java.util.List;

public class Thermostat extends SmartDevice {
    private double currentTemperature;
    private double targetTemperature;
    private HeatingMode heatingMode;
    private boolean isMaster;
    private List<Thermostat> slaveThermostats;
    private Thermostat masterThermostat;
    
    // Constructor for regular thermostat
    public Thermostat(String deviceId, String deviceName, double initialTemp) {
        super(deviceId, deviceName);
        this.currentTemperature = initialTemp;
        this.targetTemperature = 20.0;
        this.heatingMode = HeatingMode.AUTO;
        this.isMaster = false;
        this.slaveThermostats = new ArrayList<>();
        this.masterThermostat = null;
    }
    
    // Set this thermostat as master
    public void setAsMaster() {
        this.isMaster = true;
        System.out.println("[Thermostat] " + deviceName + " is now a MASTER thermostat.");
    }
    
    // Link a slave thermostat to this master
    public void addSlaveThermostat(Thermostat slave) {
        if (this.isMaster) {
            this.slaveThermostats.add(slave);
            slave.setMaster(this);
            System.out.println("[Thermostat] " + slave.getDeviceName() + " linked to master " + this.deviceName);
        } else {
            System.out.println("[Thermostat] " + this.deviceName + " is not a master. Cannot add slave.");
        }
    }
    
    // Set the master thermostat for this slave
    private void setMaster(Thermostat master) {
        this.masterThermostat = master;
        this.isMaster = false;
    }
    
    // Set target temperature
    public void setTargetTemperature(double temp) {
        this.targetTemperature = temp;
        System.out.println("[Thermostat] " + deviceName + " target temperature set to " + temp + "°C");
        
        // If this is a master, sync all slaves
        if (isMaster) {
            syncSlavesTemperature();
        }
        
        adjustHeating();
    }
    
    // Sync all slave thermostats to master's target temperature
    private void syncSlavesTemperature() {
        for (Thermostat slave : slaveThermostats) {
            slave.targetTemperature = this.targetTemperature;
            System.out.println("[Thermostat] Synced " + slave.getDeviceName() + " to " + this.targetTemperature + "°C");
            slave.adjustHeating();
        }
    }
    
    // Set heating mode
    public void setHeatingMode(HeatingMode mode) {
        this.heatingMode = mode;
        System.out.println("[Thermostat] " + deviceName + " heating mode set to " + mode);
        adjustHeating();
    }
    
    // Adjust heating based on current vs target temperature
    private void adjustHeating() {
        if (!isPoweredOn) {
            System.out.println("[Thermostat] " + deviceName + " is OFF. Cannot adjust heating.");
            return;
        }
        
        switch (heatingMode) {
            case HEATING:
                if (currentTemperature < targetTemperature) {
                    System.out.println("[Thermostat] " + deviceName + " HEATING to reach " + targetTemperature + "°C");
                } else {
                    System.out.println("[Thermostat] " + deviceName + " target reached, maintaining temperature.");
                }
                break;
            case COOLING:
                if (currentTemperature > targetTemperature) {
                    System.out.println("[Thermostat] " + deviceName + " COOLING to reach " + targetTemperature + "°C");
                } else {
                    System.out.println("[Thermostat] " + deviceName + " target reached, maintaining temperature.");
                }
                break;
            case AUTO:
                if (currentTemperature < targetTemperature) {
                    System.out.println("[Thermostat] " + deviceName + " AUTO mode: HEATING to " + targetTemperature + "°C");
                } else if (currentTemperature > targetTemperature) {
                    System.out.println("[Thermostat] " + deviceName + " AUTO mode: COOLING to " + targetTemperature + "°C");
                } else {
                    System.out.println("[Thermostat] " + deviceName + " AUTO mode: target reached.");
                }
                break;
            case OFF:
                System.out.println("[Thermostat] " + deviceName + " heating/cooling is OFF.");
                break;
        }
    }
    
    // Simulate temperature change
    public void updateCurrentTemperature(double temp) {
        this.currentTemperature = temp;
        System.out.println("[Thermostat] " + deviceName + " current temperature: " + temp + "°C");
        adjustHeating();
    }
    
    @Override
    public void turnOn() {
        super.turnOn();
        adjustHeating();
    }
    
    @Override
    public void performAction() {
        System.out.println("[Thermostat] " + deviceName + " is maintaining temperature at " + targetTemperature + "°C");
        adjustHeating();
    }
    
    @Override
    public double calculatePowerUsage() {
        if (!isPoweredOn) return 0.0;
        
        double tempDifference = Math.abs(currentTemperature - targetTemperature);
        double basePower = 50.0; // Base power consumption in watts
        double additionalPower = tempDifference * 10.0; // 10W per degree difference
        
        return basePower + additionalPower;
    }
    
    @Override
    public void executeScheduledTask() {
        System.out.println("[Thermostat] " + deviceName + " executing scheduled task: checking temperature...");
        adjustHeating();
    }
    
    @Override
    public void onEvent(Event event) {
        String eventType = event.getType();
        
        // React to temperature events from master
        if (eventType.equals("TEMPERATURE_CHANGE") && !isMaster && masterThermostat != null) {
            String extraData = event.getExtraData();
            if (!extraData.isEmpty()) {
                try {
                    double newTarget = Double.parseDouble(extraData);
                    this.targetTemperature = newTarget;
                    System.out.println("[Thermostat] " + deviceName + " received temperature sync: " + newTarget + "°C");
                    adjustHeating();
                } catch (NumberFormatException e) {
                    System.out.println("[Thermostat] Invalid temperature data received.");
                }
            }
        }
        
        // React to emergency events
        if (eventType.equals("SMOKE_DETECTED") || eventType.equals("FIRE_ALERT")) {
            System.out.println("[Thermostat] " + deviceName + " emergency detected! Shutting down HVAC.");
            this.turnOff();
        }
    }
    
    // Getters
    public double getCurrentTemperature() {
        return currentTemperature;
    }
    
    public double getTargetTemperature() {
        return targetTemperature;
    }
    
    public HeatingMode getHeatingMode() {
        return heatingMode;
    }
    
    public boolean isMaster() {
        return isMaster;
    }
    
    public List<Thermostat> getSlaveThermostats() {
        return slaveThermostats;
    }
}
