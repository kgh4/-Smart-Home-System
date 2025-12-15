package app;

import structure.Home;
import structure.Room;
import enums.RoomType;
import devices.Light;
import devices.SmartTV;
import devices.SmartLock;
import devices.sensors.MotionSensor;
import java.util.Scanner;

public class InteractiveDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Step 1: Create Home
        System.out.println("\n========== STEP 1: Create Home ==========");
        System.out.print("Enter home ID: ");
        String homeId = scanner.nextLine();
        System.out.print("Enter home name: ");
        String homeName = scanner.nextLine();
        
        Home home = new Home(homeId, homeName);
        System.out.println("✓ Home created!\n");
        
        // Step 2: Add Rooms
        System.out.println("========== STEP 2: Add Rooms ==========");
        System.out.print("How many rooms? ");
        int roomCount = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        for (int i = 0; i < roomCount; i++) {
            System.out.print("\nRoom " + (i+1) + " - Enter room ID: ");
            String roomId = scanner.nextLine();
            System.out.print("Enter room name: ");
            String roomName = scanner.nextLine();
            System.out.print("Enter room type (LIVING_ROOM/BEDROOM/KITCHEN/HALLWAY): ");
            String roomTypeStr = scanner.nextLine().toUpperCase();
            
            RoomType roomType = RoomType.valueOf(roomTypeStr);
            Room room = new Room(roomId, roomName, roomType);
            home.addRoom(room);
            System.out.println("✓ Room added!");
        }
        System.out.println("\n✓ All rooms created!\n");
        
        // Step 3: Add Devices
        System.out.println("========== STEP 3: Add Devices ==========");
        System.out.print("How many devices? ");
        int deviceCount = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 0; i < deviceCount; i++) {
            System.out.print("\nDevice " + (i+1) + " - Enter device type (Light/TV/Lock): ");
            String deviceType = scanner.nextLine().toUpperCase();
            System.out.print("Enter device ID: ");
            String deviceId = scanner.nextLine();
            System.out.print("Enter device name: ");
            String deviceName = scanner.nextLine();
            
            if (deviceType.equals("LIGHT")) {
                System.out.print("Enter brightness (0-100): ");
                int brightness = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter color temperature (WARM/NEUTRAL/COOL): ");
                String color = scanner.nextLine();
                Light light = new Light(deviceId, deviceName, brightness, color);
                
                System.out.print("Add to which room? ");
                String roomName = scanner.nextLine();
                Room room = home.getRoomByName(roomName);
                if (room != null) {
                    room.addDevice(light);
                    home.registerEventListener(light);
                    System.out.println("✓ Light added to " + roomName);
                }
            } 
            else if (deviceType.equals("TV")) {
                System.out.print("Enter min channel: ");
                int minChannel = scanner.nextInt();
                System.out.print("Enter max channel: ");
                int maxChannel = scanner.nextInt();
                scanner.nextLine();
                SmartTV tv = new SmartTV(deviceId, deviceName, minChannel, maxChannel);
                
                System.out.print("Add to which room? ");
                String roomName = scanner.nextLine();
                Room room = home.getRoomByName(roomName);
                if (room != null) {
                    room.addDevice(tv);
                    home.registerEventListener(tv);
                    System.out.println("✓ TV added to " + roomName);
                }
            }
            else if (deviceType.equals("LOCK")) {
                SmartLock lock = new SmartLock(deviceId, deviceName);
                
                System.out.print("Add to which room? ");
                String roomName = scanner.nextLine();
                Room room = home.getRoomByName(roomName);
                if (room != null) {
                    room.addDevice(lock);
                    home.registerEventListener(lock);
                    System.out.println("✓ Lock added to " + roomName);
                }
            }
        }
        System.out.println("\n✓ All devices added!\n");
        
        // Step 4: Control Devices
        System.out.println("========== STEP 4: Control Devices ==========");
        System.out.print("Turn ON all devices? (yes/no): ");
        String answer = scanner.nextLine().toLowerCase();
        if (answer.equals("yes")) {
            home.turnOnAllDevices();
            System.out.println("✓ All devices turned ON!");
        }
        
        // Step 5: Show Status
        System.out.println("\n========== STEP 5: Home Status ==========");
        home.printHomeStatus();
        
        System.out.println("\n✓ Demo Complete!");
        scanner.close();
    }
}


