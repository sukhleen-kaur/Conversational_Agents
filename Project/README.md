# Sally The Math Tutor

The math tutor is implemented with Furhat SDK. The emotion detection model is adapted from [here](https://github.com/atulapra/Emotion-detection?fbclid=IwAR3gkWvtp26fverSeZVcB8-y_Dt_k2xCV8pY7gKgExt4spN7Ai3pKQvhfhc). It was modified to include a service that can help link the model to Furhat. The emotion detection model is written in Python3.7. Read ahead to see organization and usage. 

## Folder Structure
This directory consists of two directories:
- **MathHelper**: which is the math tutor skill
- **emotiondetection**: which contains the emotion detection models

## Creating Conda Environment
The first step is to create a conda environment with the appropriate python version.
```
conda create --name NAME python=3.7
```

## Install Dependencies
In the conda env, download the required dependencies using ```pip```.
```
pip install -r requirements.txt
```

## Start the service
The first step is to run the service for which we use [Ray Serve](https://docs.ray.io/en/master/serve/).
```
ray start --head --port=6379
python predict_web_service.py
```

## Capture Video
The next step is to capture the video so the emotions can be detected.
```
python capture_video.py
```

This will start a webcam feed which can then be run in the background while the user interacts with the math tutor. 

