# Sally the Math Tutor

This project involves creating a virtual conversational math tutor. We use Furhat SDK to do this. We also make use of the emotion detection model [here](https://github.com/atulapra/Emotion-detection). This model is modified to include a service that is provided to Furhat so the agent can retrieve the user's emotion. The folder **Project** consists of:
- **MathHelper** : the math tutor skill.
- **emotiondetection** : the emotion detection model that predicts emotion in real-time.

Here, we explain how to run the emotion detection model and our virtual math tutor.

## Emotion Detection
The emotion detection model is written in Python3.7. 
### Installing Dependencies
To install the dependencies, you will need to use ```pip```.
```
pip install -r requirements.txt
```
### Running Service
We use [ray](https://docs.ray.io/en/master/serve/) serving for serving the tensorflow trained model as service to establish a service for the emotion detection model to link it to Furhat. For the first step we need to start the service and then run it for prediction.
```
ray start --head --port=6379
python predict_web_service.py
```
### Running Video Capture
We can then capture the emotion through the user's webcam as follows.
```
python capture_video.py
```

## Math Tutor
To run the math tutor, you will need access to FurhatSDK 1.21.0. The SDK can be launched as follows:
```
/bin/bash PATH_TO_SDK/launchSDK.sh
```
The MathHelper skill can then be imported into IntelliJ and then running the ```main.kt``` file will deploy the skill onto Furhat. 
