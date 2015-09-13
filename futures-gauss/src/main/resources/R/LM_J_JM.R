fire <- read.csv('F:/futures/data/model/2014-01-01_2016-09-02_2_J_JM.csv', head = T)
plot(J~JM,data=fire)
fire.reg <- lm(J~JM,fire)
summary(fire.reg)