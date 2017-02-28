This is an Android demo app to demonstrate to myself WACOM's Will SDK for "digital ink". The SDK is availble here with free registration: https://developer.wacom.com/technologies/will-universal-ink

I compiled the list of functionalities (below) as I personally understood it. This demo app attempts to use each of these functionalities. 

Will App Functionalities

##High level
- Display a list of notes

- Open, view, add to a note

- Notes composed of strokes


##Note
- Create strokes

- Manipulate Strokes - erase whole strokes using WILL's intersector

- Smoothen strokes


##File
- Notes saved on disk as WILL files

- Notes loaded from disk when opened

- Import a WILL File from filesystem
 -- Example WILL file for importing - https://github.com/skomra/InkDemo/blob/master/sample.will. Push this to your device with `adb push sample.will /storage/emulated/0/sample.will`

- Delete WILL files

##Other WILL features it would be nice to use
- erase part of a stroke instead of the whole stroke
- thumbnail images of the WILL files in the list of notes
- ink color
- utilize serialize in some way

##TODO
- plenty of FIXMEs in the code
