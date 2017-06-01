%% bSkriptRegression
% This skript realse the regression.
% @Author: Benjamin M. Abdel-Karim
% @since: 2017-05-30
% @version: 2017-05-31

%% Clear everthing
clear; clc; close all;

%% Dataimport 
% The Dataimport
%newsId,isFake,words,uppercases,questions,exclamations,authors,citations,firstperson,secondperson,thirdperson,sentencelength,repetitiveness,authorHits,sentiment
sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','sentiment'};
mData = csvread('Datenbank/2017-05-17-newsResults.csv');

%% The Regression Parameters
vY = mData(:,2);
vX = mData(:,3:end);

%% The Multiple linear regression baseline - Typ 1 Code 
% Baseline Regression in MATLAB 
[b,bint,r,rint,stats] = regress(vY,vX);

% https://de.mathworks.com/help/stats/regress.html

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
% The option setting the delimiter, and the precision!
%@code: dlmwrite - For a lot of option by saving the data
tCoefficients = mdl.Coefficients;
format short
mCoefficients = table2array(tCoefficients);
dlmwrite('Datenexporte/Coefficients.csv',mCoefficients,'delimiter',',','precision','%.5f');
% csvwrite('Datenexporte/Coefficients.csv',mCoefficients);
