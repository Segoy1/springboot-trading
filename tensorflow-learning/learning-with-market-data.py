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

batchSize = 32
T = 120 #60  gitbash run 80
epochs = 100 #500 
learningRate = 0.001 #0.002
lf = 'mae'
lstm = 200 #80 gitbash run 100
activation = 'linear'
opt=RMSprop(learning_rate=learningRate)

df = pd.read_csv("./merged.csv")

df['PREVCLOSESPX'] = df['CLOSESPX'].shift(1) # move everything up 1


df['nexttempmax'] = df['tempmax'].shift(-1) # move everything down 1
df['difftemp'] = df['nexttempmax'] - df['tempmax']

# then the return is
# (x[t] - x[t-1]) / x[t-1]
df['Return'] = (df['CLOSESPX'] - df['PREVCLOSESPX']) / df['PREVCLOSESPX'] * 20

# Now turn the full data into numpy arrays

# Not yet in the final "X" format!
input_data = df[['DATE','CLOSESPX','OPENSPX','HIGHSPX','LOWSPX',
                 'CLOSEVIX','OPENVIX','HIGHVIX','LOWVIX',
                 'CLOSETLT','OPENTLT','HIGHTLT','LOWTLT','VOLTLT',
                 'DIX','GEX','VOLUME',
                 'CLOSENASDAQ','OPENNASDAQ','HIGHNASDAQ','LOWNASDAQ',
                 'OPENXOI','CLOSEXOI','HIGHXOI','LOWXOI',
                 'difftemp','moonphase','precip','cloudcover','visibility','windspeed']].values
targets = df['Return'].values

# Now make the actual data which will go into the neural network
T = T # the number of time steps to look at to make a prediction for the next day
D = input_data.shape[1]
N = len(input_data) - T # (e.g. if T=10 and you have 11 data points then you'd only have 1 sample)

# normalize the inputs
Ntrain = len(input_data) * 6 // 10
scaler = StandardScaler()
scaler.fit(input_data[:Ntrain + T - 1])
input_data = scaler.transform(input_data)

# Setup X_train and Y_train
X_train = np.zeros((Ntrain, T, D))
Y_train = np.zeros(Ntrain)

for t in range(Ntrain):
  X_train[t, :, :] = input_data[t:t+T]
  Y_train[t] = (targets[t+T])

# Setup X_test and Y_test
X_test = np.zeros((N - Ntrain, T, D))
Y_test = np.zeros(N - Ntrain)

for u in range(N - Ntrain):
  # u counts from 0...(N - Ntrain)
  # t counts from Ntrain...N
  t = u + Ntrain
  X_test[u, :, :] = input_data[t:t+T]
  Y_test[u] = (targets[t+T])


# make the RNN

#sequential approach
model = Sequential()
model.add(LSTM(units=lstm, return_sequences=True, input_shape=(T,D)))
model.add(Dropout(0.5))
#model.add(LSTM(units=lstm,return_sequences=True))
#model.add(Dropout(0.2))
#model.add(LSTM(units=lstm,return_sequences=True))
#model.add(Dropout(0.1))
model.add(LSTM(units=lstm))
model.add(Dense(1, activation=activation))

#functional approach

#i = Input(shape=(T, D))
#x = LSTM(lstm)(i)
#x = Dense(1, activation=activation)(x)
#model = Model(i, x)
model.compile(
  loss=lf,
  optimizer=opt,
  metrics=[custom_accuracy],
)

# train the RNN
r = model.fit(
  X_train, Y_train,
  batch_size=batchSize,
  epochs=epochs,
  validation_data=(X_test, Y_test),
)

# Plot accuracy per iteration
plt.plot(r.history['custom_accuracy'], label='accuracy')
plt.plot(r.history['val_custom_accuracy'], label='val_accuracy')
plt.title(f'T({T}), BatchSize({batchSize}), learnRate({learningRate}), lstm({lstm}), loss= {lf}')
plt.legend()
plt.show()