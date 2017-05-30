%% bSkriptRegression
% This skript realse the regression.
% @Author: Benjamin M. Abdel-Karim
% @since: 2017-05-30
% @version: 2017-05-30

%% Clear everthing
clear; clc; close all;

%% Dataimport 
% The Dataimport
sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits'};
mData = csvread('Datenbank/2017-05-30-newsResults.csv');

%% The Regression Parameters
vY = mData(:,2);
vX = mData(:,3:end);

%% The Multiple linear regression baseline - Typ 1 Code 
% Baseline Regression in MATLAB 
[b,bint,r,rint,stats] = regress(vY,vX);

%% The Regression with regstats - Typ 2 Code
sRegression = regstats(vY,vX,'linear');
vBeta = sRegression.beta;
vTstat = sRegression.tstat.t;
vPVal = sRegression.tstat.pval;

%% Linears Model Class Regression - Typ 3 Code
% An object comprising training data, model description, 
% diagnostic information, and fitted coefficients for a linear regression. 
% Predict model responses with the predict or feval methods.
% https://de.mathworks.com/help/stats/linearmodel-class.html#responsive_offcanvas
mdl = fitlm(vX ,vY);
%mdl % Gibt die Daten direkt auf der Konsole aus.

% Save the Date. Step one need to typcast the table object to a matrix.
tCoefficients = mdl.Coefficients;
mCoefficients = table2array(tCoefficients);
csvwrite('Datenexporte/Coefficients.csv',mCoefficients);
