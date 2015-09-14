fire <- read.csv('F:/futures/data/model/2010-09-01_2016-09-02_2_C_CS.csv', head = T)
fire2 <- fire[-c(122,123,132,133,137,138,145),]
plot(C~CS,data=fire)
plot(C~CS,data=fire2)
fire.reg <- lm(C~CS,data=fire)
fire2.reg <- lm(C~CS,data=fire2)
summary(fire.reg)