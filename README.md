Notes:

some ble characteristics are not read-able

for example, the ble uart TX characteristic is "notify" and not "read"
so to read data from ble uart, set up notifications, descriptor, and onCharacteristicChange
however, the ble uart RX characteristic is "write"
so to write data to ble uart, perform writeCharacteristic method

