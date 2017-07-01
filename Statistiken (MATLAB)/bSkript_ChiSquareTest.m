%% Chi-2-Square-Test Matlab
clear; close all; clc;

mData = csvread('Datenbank/2017-06-21-newsResults.csv');



%% Sort the data for regression
% Sorts the data according to dependent (Y) and independent variables (X)
vY = mData(:,2);
mX = mData(:,3:end);

[h,p,stats] = chi2gof(mData(:,3));
