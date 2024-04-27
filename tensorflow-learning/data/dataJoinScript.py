
import pandas as pd


df1 = pd.read_csv("./SPX_VIX_TLT_NASDAQ_XOI_Hist.csv")
df2 = pd.read_csv("./DIX.csv")
df3 = pd.read_csv("./YSPX.csv", usecols=['DATE','VOLUME'])
weather = pd.read_csv("./New_York_City_Weather.csv")


merged = pd.merge(df1, df2, on='DATE')
merged = pd.merge(merged, df3, on='DATE')
merged = pd.merge(merged, weather, on='DATE')
merged.to_csv('./merged.csv',index=False)

# weather data from https://www.visualcrossing.com/weather/weather-data-services