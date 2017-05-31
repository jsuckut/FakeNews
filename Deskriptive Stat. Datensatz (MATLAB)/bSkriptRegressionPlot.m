%% bSkriptRegression
% This skript realse the regression plot for the variables
% @Author: Benjamin M. Abdel-Karim
% @since: 2017-05-31
% @version: 2017-05-31
% Idear Source: https://de.mathworks.com/help/stats/regress.html

%% Clear everthing
clear; clc; close all;

%% Routing Folder
addpath('Funktionsbibliothek');
addpath('Funktionsbibliothek/MATLAB2Tikiz/src');

%% Dataimport 
% The Dataimport
sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits'};
mData = csvread('Datenbank/2017-05-30-newsResults.csv');

%% The Regression Parameters
%vY = mData(:,2);
%vX = mData(:,3:end);


% Overright
  vy = mData(:,2);
  vxOne = mData(:,4);
  vxTow = mData(:,9);

% Fit-Simple Regression Modell
 X = [ones(size(vxOne)) vxOne vxTow vxOne.*vxTow];
 b = regress(vy,X);    % Removes NaN data

% Creat the 3D Scatterplot
figure;
scatter3(vxOne,vxTow,vy,'filled')
hold on
x1fit = min(vxOne):0.01:max(vxOne); 
x2fit = min(vxTow):0.01:max(vxTow);
[X1FIT,X2FIT] = meshgrid(x1fit,x2fit);
YFIT = b(1) + b(2)*X1FIT + b(3)*X2FIT + b(4)*X1FIT.*X2FIT;
mesh(X1FIT,X2FIT,YFIT)
title('Multivariate Regression');
xlabel('Variable 1');
ylabel('Variable 2');
zlabel('is FakeNews');
zticklabels({'Keine Fake News', '','','','','Fake News'});
%view(50,10)

matlab2tikz('Abbildungen/MultivariateRegression.tex');
% print -dpdf Abbildung/Wortverteilung.pdf;
