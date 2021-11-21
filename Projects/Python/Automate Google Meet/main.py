from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from time import sleep
from datetime import datetime

timeTable = ( #Time table of 6 days
  (0, 4, 3, 2),
  (0, 4, 2, 5),
  (4, 3, 5, 1),
  (2, 4, 1, 3),
  (2, 0, 5, 6),
  (0, 5, 6, 1)
)

timings = ( #Time stamps to find time slots
  [(9, x) for x in range(40, 60)] + [(10, x) for x in range(0, 41)],
  [(10, x) for x in range(41, 60)] + [(11, x) for x in range(0, 41)],
  [(11, x) for x in range(41, 60)] + [(12, x) for x in range(0, 41)],
  [(13, x) for x in range(41, 60)] + [(14, x) for x in range(0, 41)]
)

subjects = { #Dictionary to store period name
  0 : 'Java', 1 : 'Java Lab', 2 : 'Python', 3 : 'Python Lab', 4 : 'OS', 5 : 'Maths', 6 : 'EDC'
}

meetLinks = ( #Hardcoded google meet links for classes
  'https://meet.google.com/lookup/fz6iiv7bmi?authuser=0&hs=179',
  'https://meet.google.com/lookup/ddpcmwfid4?authuser=0&hs=179',
  'https://meet.google.com/lookup/b7jr42seue?authuser=0&hs=179',
  'https://meet.google.com/lookup/gwhtjvp32f?authuser=0&hs=179',
  'https://meet.google.com/lookup/aih2eikumz?authuser=0&hs=179',
  'https://meet.google.com/lookup/g5mqtb25cl?authuser=0&hs=179',
  'https://meet.google.com/lookup/cp3boga7tc?authuser=0&hs=179'
)

def googleLogin(): #Function to login in google account
  browser.get('https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin')

  emailField = browser.find_element_by_xpath('//*[@id="identifierId"]')
  emailField.send_keys('methrangs20bct025@skasc.ac.in')
  emailField.send_keys(Keys.ENTER)
  sleep(3)
  passField= browser.find_element_by_xpath('//*[@id="password"]/div[1]/div/div[1]/input')
  passField.send_keys('welcome123')
  passField.send_keys(Keys.ENTER)
  sleep(3)

def joinClass(period): #Driver function to join a class
  browser.get(meetLinks[timeTable[currentDayOrder][period]])
  sleep(2)
  browser.find_element_by_tag_name('body').send_keys(Keys.CONTROL, 'd', 'e')
  sleep(2)
  joinButton = browser.find_element_by_xpath('//*[@id="yDmH0d"]/c-wiz/div/div/div[9]/div[3]/div/div/div[4]/div/div/div[2]/div/div[2]/div/div[1]/div[1]/span')
  joinButton.click()

def getTimeSlot(currentTime): #Returns index of period (0 - 3)
  for idx, stamp in enumerate(timings):
    if currentTime in stamp:
      return idx
  return -1

#Maintain day order text file
DayOrderFile = open('dayOrder.txt', 'r+')
currentDayOrder = int(DayOrderFile.read()) - 1
print("Current Day Order:", currentDayOrder + 1)

#Handle firefox pop-ups
options = webdriver.FirefoxOptions()
options.set_preference("permissions.default.microphone", 1)
options.set_preference("permissions.default.camera", 1)

#Create an instance of webdriver
browser = webdriver.Firefox(options = options)
googleLogin()

#Main loop
ended = False

while True:
  if ended: break
  
  currentTime = (datetime.now().hour, datetime.now().minute)
  timeSlot = getTimeSlot(currentTime)

  if timeSlot != -1: #if not a break
    if timeSlot == 3: ended = True #check for last period

    date, month, year = map(int, (datetime.today().strftime('%d:%m:%Y')).split(':'))
    targetTime = datetime(year, month, date, timings[timeSlot][-1][0], timings[timeSlot][-1][1]) #current class end time
    
    print('Current period [', timeSlot + 1, ']:', subjects[timeTable[currentDayOrder][timeSlot]])
    joinClass(timeSlot) #join current class
    
    while datetime.now() < targetTime: #idle loop until end time (targerTime)
      sleep(1)
  else: #is a break
    print('No classes now.')

  sleep(60) #sleep for a minute to avoid corner case of joining a meet multiple times

print("End of day order", currentDayOrder + 1)
#Write next day order in DayOrderFile.txt
DayOrderFile.seek(0)
DayOrderFile.write(str(1 + ((currentDayOrder + 1) % 6)))

#Cleaning up
browser.close()
DayOrderFile.close()