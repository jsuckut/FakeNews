%% bSkript_MultinomialLogisticRegression
% This script implements the multinomial Logistic Regression
% This script uses the CamelCase spelling and the following conventions:
% m = Matrix
% v = vector
% s = String
% c = Cell-Array
% @source: https://de.mathworks.com/help/stats/mnrfit.html
% @author: Benjamin M. Abdel-Karim
% @since: 2017-06-06
% version 2017-06-06

clear; clc; close all;
addpath('Funktionsbibliothek/MATLAB2Tikiz/src');

%% Import the actual data
% Loading the data from sql Database csv export
% Each vector in the matrix stands for the input:
% 1 = 'newsId'
% 2 = 'isFake'
% 3 = 'words'
% 4 = 'uppercases'
% 5 = 'questions'
% 6 = 'exclamations'
% 7 = 'authors'
% 8 = 'citations'
% 9 = 'firstperson'
% 10 = 'secondperson'
% 11 = 'thirdperson'
% 12 = 'sentencelength'
% 13 = 'repetitiveness'
% 14 = 'authorHits'
% 15 = 'titleUppercase'
% 16 = 'errorLevel'
% 17 = 'sentiment'
% 18 = 'informativeness'
sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativeness'};
mData = csvread('Datenbank/2017-06-05newsResults.csv');

%% Sort the data for regression
% Sorts the data according to dependent (Y) and independent variables (X)
vY = mData(:,2);
mX = mData(:,3:end);

%% Calculate the model
% Use the function for the model - Old Version: 
% @source: https://de.mathworks.com/help/stats/generalizedlinearmodel-class.html?searchHighlight=GeneralizedLinearModel&s_tid=doc_srchtitle
cLogistischeRegression  = GeneralizedLinearModel.fit(mX,vY,'distr','binomial');



%% Plot
figure;
surf(cLogistischeRegression.CoefficientCovariance);
title('Koeffizienten Kovarianz Matrix');
grid 'on';
xlabel('Variablen');
ylabel('Variablen');
% Sava Data as TikZ
%matlab2tikz('Abbildungen/CoefficientCovarianceLogisticRegression.tex');
% print -dpdf Abbildung/Wortverteilung.pdf;

%% Fisher's exact test
% Aalso returns the significance level p of the test and a structure stats 
% containing additional test results, including the odds ratio and its 
% asymptotic confidence interval.
% @code: https://de.mathworks.com/help/stats/fishertest.html#buh29pv-7
%[h,p,stats] = fishertest(mX,'Tail','right','Alpha',0.01);


%% Export the regression results
% Save the Date. Step one need to typecast the table object to a matrix.
% The option setting the delimiter, and the precision!
% @code: dlmwrite - For a lot of option by saving the data
% @writetable(cLogistischeRegression.Coefficients,'cLogistischeRegression.Coefficients.csv');
% @code: '%.5f' - representation of accuracy
mCoefficients = table2array(cLogistischeRegression.Coefficients);
dlmwrite('Datenexporte/LogistischeRegressionCoefficients.csv',mCoefficients,'delimiter',',','precision','%.5f');


