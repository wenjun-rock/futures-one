fire <- read.csv('data.csv', head = T)
plot(X1~X2, data=fire)
sink("my.out")
fire.reg1 <- lm(X1~X2,fire)
fire.res1 <- summary(fire.reg1)
fire.res1[8]
fire.res1[4]
fire.reg2 <- lm(X2~X1,fire)
fire.res2 <- summary(fire.reg2)
fire.res2[8]
fire.res2[4]