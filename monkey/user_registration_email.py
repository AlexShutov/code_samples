# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

def isKeyboardShown():
    return 'mInputShown=true' in device.shell('dumpsys input_method')

def isActivityOnScreen(activityName):
    return activityName in device.shell("dumpsys activity activities | grep 'mResumedActivity:.*'")

def close_keyboard_if_need():
    print "close keyboard"
    device.press("KEYCODE_BACK", MonkeyDevice.DOWN_AND_UP)
    MonkeyRunner.sleep(1)

def wait_screen_for(activity_name, timout):
    print "Wait screen: %s for: %d" % (activity_name,timout)
    for i in range(0, timout):
        MonkeyRunner.sleep(1)
        if isActivityOnScreen(activity_name):
            return


# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
device.installPackage('/Users/lodoss/AndroidStudioProjects/myapp-android/monkey/install.apk')

# sets a variable with the package's internal name
packageSDK = 'com.impekable.puppyslots'
packageStore = 'com.myapp.android.market'

# sets a variable with the name of an Activity in the package
activityStore = 'com.myapp.activity.ActLaunch'
activityStoreHome = 'com.myapp.activity.ActHome'
activityStoreRegister = 'com.myapp.activity.ActRegister'
activityStoreLogin = 'com.myapp.activity.ActRegister'
activityStoreTutorial = 'com.myapp.activity.ActTutorialScreen'

# sets the name of the component to start
runComponentStore = packageStore + '/' + activityStore

# Runs Store app
device.startActivity(component=runComponentStore)

def login():
    #click facebook
    print "click facebook"
    device.touch(611, 611, 'DOWN_AND_UP')
    MonkeyRunner.sleep(1)
    print "click facebook"
    device.touch(556, 502, 'DOWN_AND_UP')
    MonkeyRunner.sleep(1)
    print "click facebook"
    device.touch(556, 502, 'DOWN_AND_UP')
    MonkeyRunner.sleep(1)
    print "click facebook"
    device.touch(556, 502, 'DOWN_AND_UP')
    MonkeyRunner.sleep(1)
    print "click facebook"
    device.touch(556, 502, 'DOWN_AND_UP')

    #wait chosing dialog
    print "wait chosing dialog"
    MonkeyRunner.sleep(10)

    #click proceed
    print "click proceed"
    device.touch(865, 1494, 'DOWN_AND_UP')
    MonkeyRunner.sleep(1)

    #wait facebook activity
    print "wait facebook activity"
    MonkeyRunner.sleep(10)

    #click on login field
    print "click on login field"
    device.touch(447, 859, 'DOWN_AND_UP')

    #wait keyboard
    print "wait keyboard"
    MonkeyRunner.sleep(2)
    print "type email"
    device.type("lodossgarmash.a@gmail.com")

    #click on password field
    print "click on password field"
    device.touch(420, 1011, 'DOWN_AND_UP')

    #wait keyboard
    print "wait keyboard"
    MonkeyRunner.sleep(2)
    print "type password"
    device.type("j2F98k23")

    #click "Go"
    MonkeyRunner.sleep(2)
    print "click 'Go'"
    device.touch(971, 1841, 'DOWN_AND_UP')

    #click LOGIN
    MonkeyRunner.sleep(2)
    print "click LOGIN"
    device.touch(570, 1141, 'DOWN_AND_UP')

    #wait facebook and click ok
    print "wait facebook and click ok"
    MonkeyRunner.sleep(10)
    device.touch(745, 1787, 'DOWN_AND_UP')

    #click anywhere
    MonkeyRunner.sleep(10)

    isActivityOnScreen()

    #input pin
    print "input pin"
    MonkeyRunner.sleep(10)
    device.type("2514")

    #repeat pin
    print "repeat pin"
    MonkeyRunner.sleep(5)
    device.type("2514")

    if isActivityOnScreen(activityStoreHome):
        print "Home screen achieved!"
    else:
        raise Exception("ERROR! Something went wrong! 2")

def remove_all_payment_methods():
    MonkeyRunner.sleep(2)
    print "Open navdrawer"
    device.touch(75, 158, 'DOWN_AND_UP')
    MonkeyRunner.sleep(2)
    print "Click 'My account'"
    device.touch(285, 624, 'DOWN_AND_UP')
    MonkeyRunner.sleep(4)
    print "Click 'Payment Methods'"
    device.touch(547, 640, 'DOWN_AND_UP')
    MonkeyRunner.sleep(4)

    for i in range(0,10):
        print "Click 'Delete' on card"
        device.touch(941, 898, 'DOWN_AND_UP')
        MonkeyRunner.sleep(1)
        print "Click 'yes'"
        device.touch(852, 1219, 'DOWN_AND_UP')
        print "Wait deletion"
        MonkeyRunner.sleep(6)

MonkeyRunner.sleep(5)

if isActivityOnScreen(activityStore):
    print "Store activity is on screen"
elif isActivityOnScreen(activityStoreTutorial):
    print "Tutorial screen loaded"
else:
    print "Activity isn't loaded"