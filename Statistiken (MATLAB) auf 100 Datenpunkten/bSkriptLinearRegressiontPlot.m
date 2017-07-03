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
%sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativeness'};

sVar = {'$X_{1}$ words','$X_{2}$ uppercases','$X_{3}$ questions',...
    '$X_{4}$ exclamations','$X_{5}$ authors','$X_{6}$ citations',...
    '$X_{7}$ firstperson','$X_{8}$ secondperson', '$X_{9} $thirdperson',...
    '$X_{10}$ sentencelength','$X_{11}$ repetitiveness','$X_{12}$ authorHits','$X_{13}$ titleUppercase',...
    '$X_{14}$ errorLevel','$X_{15}$ sentiment','$X_{16}$ informativeness','$X_{17}$ super. per Words ','$X_{18}$ super. per Adj.'...
      '$X_{19}$usedsources', '$X_{20}$internsources','$X_{21}$externsources','$X_{22}$usedimages'};
  
 % mData = csvread('Datenbank/2017-06-21-newsResults.csv');
 
 mData = csvread('Datenbank/2017-07-02-newsResults.csv');

%% The Regression Parameters
%vY = mData(:,2);
%vX = mData(:,3:end);


% Overright
% 2017-05-30 - 2, 4, 9
% 2017-06-06 - 2, 8, 15
  vy = mData(:,2);
  vxOne = mData(:,5);
  vxTow = mData(:,20);

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
title('Multivariate logistische Regression');
xlabel('authors');
ylabel('intern Source');
zlabel('is FakeNews');
zticklabels({'wahre Nachricht', '','','','','Fake News'});
view(-18,25)

matlab2tikz('Abbildungen/MultivariateRegression.tex');
% print -dpdf Abbildung/Wortverteilung.pdf;
