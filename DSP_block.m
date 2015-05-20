clear all;
close all;
clc;

cd C:\Users\Jangs\Documents\JangProject;

TESTCASE = 1;
down_N = 3;                            % 다운 샘플링 계수
FILTER_TYPE = 2;                       % 1 == mean, 2 == linear, 3 == nevile
datatype = 3;                          % 1 == uint8, 2 == int16, 3 == float
BPFtype = 3;                           % 1 == AM Radio 2 == FM Radio 3 == NTSC TV Audio 

fprintf('Down_sampling_Level : %d, Filter Type : %d, Data type : %d\n',down_N,FILTER_TYPE, datatype);

%% LPF 만들기
if(BPFtype == 1)
[N,fpts,mag,wt]=firpmord([1980 3020],[1 0],[0.0005 0.0005],22050);
[q0,err]=firpm(N,fpts,mag,wt);
q0(40)=q0(40)+err;
q0 = single(q0);
h0=firminphase(q0);

figure(1);
H0  = freqz(h0,1,22050);
plot(abs(H0),'r');
title('AM 라디오 100Hz ~ 5,000 Hz , Filter coefficient 40'); 

elseif(BPFtype == 2)  
[N,fpts,mag,wt]=firpmord([4480 5520],[1 0],[0.0005 0.0005],22050);
[q0,err]=firpm(N,fpts,mag,wt);
q0(40)=q0(40)+err;
q0 = single(q0);
h0=firminphase(q0);

figure(1);
H0  = freqz(h0,1,22050);
plot(abs(H0),'r');
title('FM 라디오 100Hz ~ 10,000 Hz , Filter coefficient 40');

elseif(BPFtype == 3)
[N,fpts,mag,wt]=firpmord([6980 8020],[1 0],[0.0005 0.0005],22050);
[q0,err]=firpm(N,fpts,mag,wt);
q0(40)=q0(40)+err;
q0 = single(q0);
h0=firminphase(q0);

[N,fpts,mag,wt]=firpmord([5250 9750],[1 0],[0.0005 0.0005],22050);
[q0,err]=firpm(N,fpts,mag,wt);
q0(9)=q0(9)+err;
q0 = single(q0);
h0=firminphase(q0);


figure(1);
H0  = freqz(h0,1,22050);
plot(abs(H0),'r');
title('NTSC TV 오디오 80Hz ~ 15,000 Hz , Filter coefficient 40');
end

%% 음성신호 읽기 (16bit -> 8bit)
if(TESTCASE == 1)
    [x_16,fs,bit]=wavread('testcase1.wav','native'); % read speaker signal
end
if(TESTCASE == 2)
    [x_16,fs,bit]=wavread('testcase2.wav','native'); % read speaker signal
end
if(TESTCASE == 3)
    [x_16,fs,bit]=wavread('testcase3.wav','native'); % read speaker signal
end
if(TESTCASE == 4)
    [x_16,fs,bit]=wavread('testcase4.wav','native'); % read speaker signal
end
if(TESTCASE == 5)
    [x_16,fs,bit]=wavread('testcase5.wav','native'); % read speaker signal
end

x_16 = x_16(1:4096*2000);
% 16 bit -> 8 bit
con_x = int32(x_16) + 32768;
x_8 =  uint8((con_x ./256));  % 캐스팅시 데이터가 손상된다. 1손상 256 -> 255
if(datatype == 3)
    x_float = single(x_16)/32768;
end

x_16 = int16(x_8)
%% 음성신호 실시간 처리
% Buffer 사용


%% 음성신호 DowmSampling (구현 시 N 조절가능하도록 만들기) 직접 구현
%아직 안정화 안됨!


total_sample = length(x_8);            % 총샘플링 숫자
ndown_sample =  total_sample/down_N;   % 다운샘플링해서 남는 숫자
down_x = uint8(zeros(1,ndown_sample))';        % 다운샘플링 넣을 배열 할당

for i = 1:ndown_sample
    down_x(i) = x_8(i*down_N);
end

%down_api_x = downsample(x_8,4);
%% White Noise 통과시키기

%% 음성신호 UpSampling (구현 시 N 조절가능하도록 만들기)
%up_api_x = upsample(down_x,down_N);
if(datatype == 1)
    up_x = uint8(zeros(1,total_sample))';
    for i = 1:ndown_sample
        up_x(down_N*i) = down_x(i);
    end
end

if(datatype == 3)
    up_x = single(zeros(1,total_sample))';  % float data로 변형
    for i = 1:ndown_sample
        up_x(down_N*i) = (single(down_x(i)) - 128)/128;
    end
end



%% interpolation
if(FILTER_TYPE == 1)
    % Mean filter
    fprintf('Mean_filter\n');
    interp_x =  mean_interpolation_filter(up_x,down_N,ndown_sample,datatype);
end
if(FILTER_TYPE == 2)
    % Linear filter
    fprintf('Linear_filter\n');
    interp_x =  linear_interpolation_filter(up_x,down_N,ndown_sample,datatype);
end
if(FILTER_TYPE == 3)
    % Nevile filter
    fprintf('Nevile_filter\n');
    interp_x =  nevile_interpolation_filter(up_x,down_N,ndown_sample);
end



if(datatype == 1)
    %% 8bit->int16 확장 모듈 구현
    
    con_x = int32(interp_x) * 256;   % 0 - 65280
    con_x = int16(con_x - 32768);
    
    %%  Data Check기 구현 (전체 샘플수에 대한)
    diff= sum(abs(x_8  - interp_x));
    fprintf('Error rate : %s\n',diff/total_sample);
end
if (datatype == 3)
    con_x = interp_x;
    %%  Data Check기 구현 (전체 샘플수에 대한)
    diff= sum(abs(x_float  - con_x));
    fprintf('Error rate : %s\n',diff/total_sample);
end

%% BPF 적용시키기 
tic();
result_x = conv(con_x,h0); 
toc();
 %%  Data Check기 구현 (전체 샘플수에 대한)
    diff= sum(abs(x_float  - result_x(1:total_sample)));
    fprintf('(After_BPF) Error rate : %s\n',diff/total_sample);

figure(2);

%stem(con_x(30000:31000))
player = audioplayer(con_x,fs);
play(player);
% stop(player);

%% 속도테스트 및 오차율 측정,방법





