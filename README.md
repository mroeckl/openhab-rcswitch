# openhab-rcswitch
Binding to control RC switches in OpenHAB

## Configuration
```
rcswitch:gpioPin=0
```

## Items
```
Switch	SleepingRoom	{rcswitch="11111:4"}
Switch	LivingRoom		{rcswitch="11111:1"}
```

## Sitemap
```
Switch item=SleepingRoom label="Sleeping Room"
Switch item=LivingRoom label="Living Room"
```