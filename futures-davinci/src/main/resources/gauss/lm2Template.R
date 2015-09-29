fire <- read.csv('data.csv', head = T)
plot(X1~X2, data=fire)
fire.reg <- lm(X1~X2,fire)
summary(fire.reg)
sink("my.out")
fire.res <- summary(fire.reg)
fire.res[8]
fire.res[4]