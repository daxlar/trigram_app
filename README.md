# trigram_app

Overview:    
  
trigram_app is the partner app to the trigram device, that helps people track how well they are socially distancing themselves       
It connects to the trigram device over BLE UART and collects the bluetooth mac addresses that the trigram devices sends over        
The greater the density of the devices seen, the greater likelihood that a BLE CENTRAL device, like a smartphone is approaching you      
This app displays the amount of devices seen every second on a graph UI, and logs encounters in a Firebase database    
We can approximate that one person ~ one phone    
This project works best in open environments!      
  
Getting started:    

git clone this repository      
Open trigram_app in Android Studio    
Create your own firebase database and replace google-services.json with your own    
You can follow steps here:    
[Firebase Real-Time Database](https://firebase.google.com/docs/database/android/start)    


Operation process:    

Open trigram_app and you should be greeted with:  
<img src="https://github.com/daxlar/trigram_app/blob/master/pictures/greeting.jpg" width="200" height="400">  
Click scan and connect to NORDIC_UART:    
<img src="https://github.com/daxlar/trigram_app/blob/master/pictures/scan.jpg" width="200" height="400">  
The GraphView UI displays how many devices seen nearby:    
<img src="https://github.com/daxlar/trigram_app/blob/master/pictures/connected.jpg" width="200" height="400">  
Open your Firebase Real-Time Database and it should look like this, where each time stamp represents devices seen at that moment in time:  
<img src="https://github.com/daxlar/trigram_app/blob/master/pictures/firebase_screenshot.png" width="1200" height="350"> 

Technical notes:  

some ble characteristics are not "read"-able

for example, the ble uart TX characteristic is "notify" and not "read"
so to read data from ble uart, set up notifications, descriptor, and onCharacteristicChange
however, the ble uart RX characteristic is "write" with no descriptor
so to write data to ble uart, perform writeCharacteristic method

