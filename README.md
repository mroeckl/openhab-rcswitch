# openhab-rcswitch
Binding to control RC switches in OpenHAB

## Configuration
```
rcswitch:SleepingRoom.gpioPin=0
rcswitch:SleepingRoom.groupAddress=11111
rcswitch:SleepingRoom.deviceAddress=1
rcswitch:LivingRoom.gpioPin=0
rcswitch:LivingRoom.groupAddress=11111
rcswitch:LivingRoom.deviceAddress=4
```

## Items
```
Switch	SleepingRoom	{rcswitch="ON:SleepingRoom, OFF:SleepingRoom"}
Switch	LivingRoom		{rcswitch="ON:LivingRoom, OFF:LivingRoom"}
```

## Sitemap
```
Switch item=SleepingRoom label="Sleeping Room"
Switch item=LivingRoom label="Living Room"
```