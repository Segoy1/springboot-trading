import tensorflow as tf
# More imports
from tensorflow.keras.layers import Input, LSTM, GRU, SimpleRNN, Dense, GlobalMaxPool1D, Dropout
from tensorflow.keras.models import Model
from tensorflow.keras.optimizers import SGD, Adam, RMSprop
from keras.models import Sequential

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler

def custom_accuracy(y_true, y_pred, tolerance=0.002 * 20):
    correct = tf.abs(y_true - y_pred) <= tolerance
    return tf.reduce_mean(tf.cast(correct, tf.float32))

batchSize = 64
T = 120 
epochs = 300 
learningRate = 0.0001
lstm = 64
opt=RMSprop(learning_rate=learningRate)
#multiplier to kind of normalize the data
multi = 20


#Upper for values lower two for ranges(positive/negative..)
#lf = 'mae'
#activation = 'tanh'
lf = 'binary_crossentropy'
activation = 'sigmoid'

df = pd.read_csv("./merged.csv")


def custom_accuracy(y_true, y_pred, tolerance=0.002 * multi):
    correct = tf.abs(y_true - y_pred) <= tolerance
    return tf.reduce_mean(tf.cast(correct, tf.float32))

df['PREVCLOSESPX'] = df['CLOSESPX'].shift(1) # move everything up 1


df['nexttempmax'] = df['tempmax'].shift(-1) # move everything down 1
df['difftemp'] = df['nexttempmax'] - df['tempmax']

# then the return is
# (x[t] - x[t-1]) / x[t-1]
df['Return'] = (df['CLOSESPX'] - df['PREVCLOSESPX']) / df['PREVCLOSESPX'] * multi


series = pd.Series(df['CLOSESPX'])
df['SPXM26'] = series.ewm(26, adjust=False).mean()
df['SPXM26S'] = series.ewm(26, adjust=False).std()
df['SPXM12'] = series.ewm(12, adjust=False).mean()
df['SPXM12S'] = series.ewm(12, adjust=False).std()

df['SPXM15'] = df['CLOSESPX'].rolling(15).mean()
df['SPXM15S'] = df['CLOSESPX'].rolling(15).std()
df['SPXM50'] = df['CLOSESPX'].rolling(50).mean()
df['SPXM50S'] = df['CLOSESPX'].rolling(50).std()

df['VIXM26'] = df['CLOSEVIX'].rolling(26).mean()
df['VIXM26S'] = df['CLOSEVIX'].rolling(26).std()
df['VIXM12'] = df['CLOSEVIX'].rolling(12).mean()
df['VIXM12S'] = df['CLOSEVIX'].rolling(12).std()

df['NASDAQM26'] = df['CLOSENASDAQ'].rolling(26).mean()
df['NASDAQM26S'] = df['CLOSENASDAQ'].rolling(26).std()
df['NASDAQM12'] = df['CLOSENASDAQ'].rolling(12).mean()
df['NASDAQM12S'] = df['CLOSENASDAQ'].rolling(12).std()

df['TLTM26'] = df['CLOSETLT'].rolling(26).mean()
df['TLTM26S'] = df['CLOSETLT'].rolling(26).std()
df['TLTM12'] = df['CLOSETLT'].rolling(12).mean()
df['TLTM12S'] = df['CLOSETLT'].rolling(12).std()
df['TLTM15'] = df['CLOSETLT'].rolling(15).mean()
df['TLTM15S'] = df['CLOSETLT'].rolling(15).std()
df['TLTM50'] = df['CLOSETLT'].rolling(50).mean()
df['TLTM50S'] = df['CLOSETLT'].rolling(50).std()

df['GOLDENCROSSSPX'] = df['SPXM15']-df['SPXM50']

# Now turn the full data into numpy arrays

# Not yet in the final "X" format!
input_data = df[['DATE','CLOSESPX','OPENSPX',
                 'CLOSEVIX','OPENVIX',
                 'CLOSETLT','OPENTLT','VOLTLT',
                 'DIX','GEX','VOLUME',
                 'OPENNASDAQ','CLOSENASDAQ',
                 'OPENXOI','CLOSEXOI','moonphase','precip','nexttempmax',
                 'TLTM26','TLTM26S','TLTM12','TLTM12S', 'TLTM15','TLTM15S','TLTM50','TLTM50S',
                 'SPXM26','SPXM26S','SPXM12','SPXM12S', 'SPXM15','SPXM15S','SPXM50','SPXM50S', 'GOLDENCROSSSPX',
                 'NASDAQM26','NASDAQM26S','NASDAQM12','NASDAQM12S',
                 'VIXM26','VIXM26S','VIXM12','VIXM12S'                 
                 ]].values
targets = df['Return'].values

input_data = input_data[50:]
targets= targets[50:]

# Now make the actual data which will go into the neural network
T = T # the number of time steps to look at to make a prediction for the next day
D = input_data.shape[1]
N = len(input_data) - T # (e.g. if T=10 and you have 11 data points then you'd only have 1 sample)

# normalize the inputs
Ntrain = len(input_data) * 7 // 10
scaler = StandardScaler()
scaler.fit(input_data[:Ntrain + T - 1])
input_data = scaler.transform(input_data)




# Setup X_train and Y_train
X_train = np.zeros((Ntrain, T, D))

noise_std = 0.1
# Generate Gaussian noise with the same shape as the training data
noise = np.random.normal(loc=0, scale=noise_std, size=X_train.shape)

# Add the noise to the training data
X_train = X_train + noise

Y_train = np.zeros(Ntrain)

for t in range(Ntrain):
  X_train[t, :, :] = input_data[t:t+T]
  #Y_train[t] = (targets[t+T])
  #Y_train[t] = (targets[t+T] > 0)
  Y_train[t] = (np.abs(targets[t+T]) > (0.007 * multi))

# Setup X_test and Y_test
X_test = np.zeros((N - Ntrain, T, D))
Y_test = np.zeros(N - Ntrain)

for u in range(N - Ntrain):
  # u counts from 0...(N - Ntrain)
  # t counts from Ntrain...N
  t = u + Ntrain
  X_test[u, :, :] = input_data[t:t+T]
  
  #One of the following
  #Y_test[u] = (targets[t+T])
  #Y_test[u] = (targets[t+T] > 0)
  Y_test[u] = (np.abs(targets[t+T]) > (0.007 * multi))

# make the RNN

#'''
#Sequential approach
model = Sequential([
    Input(shape=(T,D)),
    LSTM(units=lstm, return_sequences=True),
    Dropout(0.2),
    #LSTM(units=lstm, return_sequences=True),
    #Dropout(0.2),
    #LSTM(units=lstm, return_sequences=True),
    #Dropout(0.3),
    LSTM(units=lstm),
    Dense(1, activation=activation)
])

'''
#functional approach

i = Input(shape=(T, D))
x = LSTM(lstm)(i)
x = Dense(1, activation=activation)(x)
model = Model(i, x)

'''

model.compile(
  loss=lf,
  optimizer=opt,
  #metrics=[custom_accuracy]
  metrics=['accuracy'],
)

# train the RNN
r = model.fit(
  X_train, Y_train,
  batch_size=batchSize,
  epochs=epochs,
  validation_data=(X_test, Y_test),
)



pred = model.predict(X_test)

pred = pred.T

#'''
binary_predictions = (pred >= 0.5).astype(int)

# Count the number of correct predictions
pred_s = np.sum(binary_predictions == Y_test)
diff = pred_s / len(Y_test)
'''

diff = np.mean(np.abs(Y_test - pred)) / multi

'''

print("Actual Prediction Accuracy", diff )
#print(np.sum(np.abs(Y_test) > 0.007 * multi  ), np.sum(Y_test))
print(np.sum(Y_test == 1)/len(Y_test))


# Plot accuracy per iteration
plt.plot(r.history['accuracy'], label='accuracy')
plt.plot(r.history['val_accuracy'], label='val_accuracy')
plt.title(f'T({T}), BatchSize({batchSize}), learnRate({learningRate}), lstm({lstm}), loss= {lf}')
plt.legend()
plt.show()

'''
from statsmodels.tsa.stattools import adfuller

df['CLOSESPXM26'] = df['CLOSESPX'].rolling(26).mean()

df['CLOSESPXMS'] = df['CLOSESPX'].rolling(26).std()
df['CLOSESPXM12'] = df['CLOSESPX'].rolling(12).mean()

dftest = adfuller( (np.log(df['CLOSESPXMS'][26:])), autolag='AIC')
dfoutput = pd.Series(dftest[0:4], index=['Test Statistic','p-value','#Lags Used','Number of Observations Used'])
    
for key,value in dftest[4].items():
        dfoutput['Critical Value (%s)'%key] = value
    
print(dfoutput)
'''