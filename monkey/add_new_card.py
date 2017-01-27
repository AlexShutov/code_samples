import string
import re
import time
# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

def isKeyboardShown():
    return 'mInputShown=true' in device.shell('dumpsys input_method')

def isActivityOnScreen(activityName):
    return activityName in device.shell("dumpsys activity activities | grep 'mResumedActivity:.*'")

def isFragmentActive(fragmentName):
    return fragmentName in device.shell("dumpsys activity com.betcade.activity")

def close_keyboard_if_need():
    print "close keyboard"
    device.press("KEYCODE_BACK", MonkeyDevice.DOWN_AND_UP)
    MonkeyRunner.sleep(1)

def hide_keyboard():
    if isKeyboardShown():
        print "Keyboard is shown, hiding it"
        device.press("KEYCODE_BACK", MonkeyDevice.DOWN_AND_UP)
        MonkeyRunner.sleep(0.5)

def wait_screen_for(activity_name, timout):
    print "Wait screen: %s for: %d" % (activity_name,timout)
    for i in range(0, timout):
        MonkeyRunner.sleep(1)
        if isActivityOnScreen(activity_name):
            return


# extract Fragment name from log related to this Fragment
def get_fragment_name(thisFragmentLog):
    regex2 = r"(.*){(.*)}"
    matchObj = re.match(regex2, thisFragmentLog)
    if matchObj:
        name = matchObj.group(1)
        # remove #2: text - 4 characters
        name = name[4:]
        printL ("found: " + name)
        return name
    else:
        return None
# figure out if fragment is visible
def get_fragment_visibility(thisFragmentLog):
    # get mUserVisibleHint field value
    visRegex = r'mUserVisibleHint=(.*)'
    visSearch = re.search(visRegex, thisFragmentLog)
    if visSearch:
        state = visSearch.group(1)
        # last symbol is new line character - remove it
        state = state[0: len(state) -1]
        printL("found:" + state);
        return state == "true"
    else:
        printL ("not found")
        return False

def is_fragment_on_screen(fragmentName):
    # log from adb
    log = device.shell('dumpsys activity com.betcade.activity')
    # find out if log has begin and end text, surrouning 'fragment' section
    begPos = string.find(log, begin)
    endpos = string.find(log, end)
    fragmentLog = log[begPos:endpos]
    printL("fragments section: " + fragmentLog)
    regex = r'\#\d:\s';
    p = re.compile(regex)
    indices = []
    for m in p.finditer(fragmentLog):
        i = m.start()
        indices.append(i)
        # print i, m.group()
    indices.append(len(fragmentLog) - 1)
    # if there is no fragments at all
    if len(indices) == 1:
        print "There is no fragments"
        return False
    printL (indices)
    fragmentVisibility = {}
    for i in range(0, len(indices) -1):
        # get log for i'th element from entire log by using indexes
        l = fragmentLog[indices[i] : indices[i + 1]]

        currName = get_fragment_name(l)
        isVisible = False
        if currName:
            printL ("fragment name: " + currName)
            isVisible = get_fragment_visibility(l)
            if isVisible:
                printL ("is visible")
            else:
                printL ("is not visible")
            fragmentVisibility[currName] = isVisible
        else:
            printL ("fragment name isn't found")
        printL(l)
    # check if requested Fragment were found
    if fragmentName in fragmentVisibility.keys():
        printL("There is fragment: " + fragmentName)
        return fragmentVisibility[fragmentName]
    else:
        printL("There is no such a fragment: " + fragmentName)
        return False


# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
device.installPackage('/Users/lodoss/AndroidStudioProjects/BetCade-android/monkey/install.apk')

# sets a variable with the package's internal name
packageSDK = 'com.impekable.puppyslots'
packageStore = 'com.betcade.android.market'

# sets a variable with the name of an Activity in the package
activityStore = 'com.betcade.activity.ActLaunch'
activityStoreHome = 'com.betcade.activity.ActHome'
activityStoreRegister = 'com.betcade.activity.ActRegister'
activityStoreLogin = 'com.betcade.activity.ActRegister'
activityStoreTutorial = 'com.betcade.activity.ActTutorialScreen'


fragmentFeatured = "FragHomeFeatured"
fragmentPopular = "FragHomePopular"
fragmentLive = "FragHomeLive"

begin = "Active Fragments in"
end = "Added Fragments:"


# sets the name of the component to start
runComponentStore = packageStore + '/' + activityStore

# Runs Store app
device.startActivity(component=runComponentStore)

# print verbose messages into log ot not
showMsgs = False
def printL(message):
    if showMsgs:
        print message;


def wait_for_tutorial_screen():
    MonkeyRunner.sleep(10)
    print "Waiting for tutorial screen after startup"
    if isActivityOnScreen(activityStore):
        raise Exception("Splash screen hasn't gone away")
    elif isActivityOnScreen(activityStoreTutorial):
        print "Splash screen gone away, not logged in- tutorial screen is active"
        raise Exception("User isn't logged in")
    else:
        print "User is logged in"
        add_new_card()

# card data
phone = '44055123412341234'
email = 'user@mail.com'
nameOnCard = 'user user'
cardNumber = '5000 0000 0000 0421'
postalCode = 'TE456ST'
cvv = '123'
cardNoBlock_1 = '5000'
cardNoBlock_2 = '0000'
cardNoBlock_3 = '0000'
cardNoBlock_4 = '0611'
firstName = 'abc'
lastName = 'abc'
setAsDefault = True
# billing information
houseNo = '789'
street = 'Main'
flatNo = '1'
town = 'London'
postalCode = 'TE456ST'

def add_new_card():
    open_add_card_screen()
    fill_in_user_information()
    fill_in_card_info()
    fill_in_billing_address()

def open_add_card_screen():
    # open navigation drawer
    device.touch(117, 193, "DOWN_AND_UP")
    MonkeyRunner.sleep(1)
    # drag navigation drawed down to the end
    a = (400, 1600)
    b = (400, 700)
    device.drag(a, b, 1, 10)
    MonkeyRunner.sleep(3)
    # click My Account
    device.touch(500, 1570, "DOWN_AND_UP")
    MonkeyRunner.sleep(2)
    # click payment methods
    device.touch(535, 656, "DOWN_AND_UP")
    MonkeyRunner.sleep(2)

    # click add new card
    device.touch(535, 1800, "DOWN_AND_UP")
    MonkeyRunner.sleep(1)

def fill_in_user_information():
    print 'Filling in user information'
    # fill in 'Mobile number' field
    device.touch(500, 425, "DOWN_AND_UP")
    device.type(phone)
    MonkeyRunner.sleep(1)
    # fill in Email field
    device.touch(500, 580, "DOWN_AND_UP")
    device.type(email)
    MonkeyRunner.sleep(1)

    # tap birthdate field
    device.touch(500, 730, "DOWN_AND_UP")
    MonkeyRunner.sleep(1)
    # edit birth year
    print "selecting birth year"
    device.touch(525, 800, "DOWN_AND_UP")
    # drag year picker fast
    a = (525, 900)
    b = (525, 1500)
    MonkeyRunner.sleep(0.5)
    device.drag(a, b ,0.15,5)
    MonkeyRunner.sleep(0.5)
    device.drag(a, b ,0.15,5)
    MonkeyRunner.sleep(0.5)
    device.drag(a, b ,0.15,5)
    MonkeyRunner.sleep(0.5)
    device.drag(a, b ,0.15,5)
    MonkeyRunner.sleep(0.5)
    # select 1988
    device.touch(535, 1320, "DOWN_AND_UP")
    MonkeyRunner.sleep(0.5)

    # press 'Ok'
    print "Pressing 'Ok'"
    device.touch(785, 1690, "DOWN_AND_UP")
    MonkeyRunner.sleep(0.5)
    # hide keyboard by pressing 'Back'
    hide_keyboard()
    # click 'Continue' button
    print "Clicking 'Continue' button"
    device.touch(520, 900, "DOWN_AND_UP")
    MonkeyRunner.sleep(0.5)

def type_string_by_letters(s):
    print "typing string by letters: " + s
    l = list(s)
    for i, val in enumerate(l):
        device.type(val)
        MonkeyRunner.sleep(0.5)

def fill_in_card_info():
    print 'Adding payment method'

    # touch 'Name on card' field
    print "Entering 'Name on card' field"
    device.touch(230, 400, "DOWN_AND_UP")
    MonkeyRunner.sleep(0.5)
    type_string_by_letters(firstName)
    device.press('KEYCODE_SPACE', MonkeyDevice.DOWN_AND_UP)
    type_string_by_letters(lastName)
    hide_keyboard()
    MonkeyRunner.sleep(1)


    # touch 'Card number' field
    print "entering card number"
    device.touch(150, 550, "DOWN_AND_UP")
    print "click on 'Card Number'"
    device.touch(1295, 352, 'DOWN_AND_UP')
    MonkeyRunner.sleep(1)

    print "input 'Card Number' field 1"
    device.type(cardNoBlock_1)
    MonkeyRunner.sleep(1)

    print "input 'Card Number' field 2"
    device.type(cardNoBlock_2)
    MonkeyRunner.sleep(1)

    print "input 'Card Number' field 3"
    device.type(cardNoBlock_3)
    MonkeyRunner.sleep(1)

    print "input 'Card Number' field 4"
    device.type(cardNoBlock_4)
    MonkeyRunner.sleep(1)

    # touch 'cvv' field
    print "entering 'cvv'"
    device.touch(230, 720, "DOWN_AND_UP")
    device.type(cvv)
    MonkeyRunner.sleep(1)

    # touch 'Expiration date' field
    print 'selecting expiration date'
    device.touch(750, 730, "DOWN_AND_UP")
    MonkeyRunner.sleep(3)
    a = (730, 1160)
    b = (730, 970)
    MonkeyRunner.sleep(0.5)
    device.drag(a, b ,0.15,5)
    MonkeyRunner.sleep(0.5)

    # press 'add' button
    print "pressing 'add' button"
    device.touch(840, 1340, "DOWN_AND_UP")

    # press 'Continue' button
    MonkeyRunner.sleep(0.5)
    device.touch(550, 1020, "DOWN_AND_UP")

    if setAsDefault:
        print "clicking 'set as default' checkbox"
        device.touch(71, 810, "DOWN_AND_UP")


def erase_text_field():
    MonkeyRunner.sleep(0.5)
    for i in range(0, 10):
        device.touch(975, 1680, "DOWN_AND_UP")
        MonkeyRunner.sleep(0.5)
    hide_keyboard()

def fill_in_billing_address():
    print "Filling in billing address"
    hide_keyboard()

    # house no and street address
    print "Entering house NO and street address"
    device.touch(160, 400, "DOWN_AND_UP")
    device.touch(160, 400, "DOWN_AND_UP")
    MonkeyRunner.sleep(0.5)
    type_string_by_letters(houseNo)
    print "pressing space"
    device.press('KEYCODE_SPACE', MonkeyDevice.DOWN_AND_UP)
    MonkeyRunner.sleep(0.5)
    type_string_by_letters(street)

    # flat number
    print "entering flat no"
    device.touch(230, 560, "DOWN_AND_UP")
    type_string_by_letters(flatNo)

    # town
    print "entering town field"
    device.touch(150, 740, "DOWN_AND_UP")
    type_string_by_letters(town)

    #postal code
    print "touching 'postal code' field"
    device.touch(310, 900, "DOWN_AND_UP")
    erase_text_field()
    type_string_by_letters(postalCode)
    hide_keyboard()

    # selecting country
    print "Selecting country"
    device.touch(750, 900, "DOWN_AND_UP")
    # wait for dialog to appear
    MonkeyRunner.sleep(2)
    # press 'UK'
    print "Press 'UK'"
    device.touch(400, 600, "DOWN_AND_UP")
    # press 'ok'
    print "Press 'OK'"
    device.touch(550, 1600, "DOWN_AND_UP")
    MonkeyRunner.sleep(1)

    # press 'Confirm' button
    print "Press 'Confirm' button"
    device.touch(530, 1080, "DOWN_AND_UP")


wait_for_tutorial_screen()