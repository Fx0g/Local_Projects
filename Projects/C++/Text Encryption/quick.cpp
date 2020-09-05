#include<iostream>
#include<fstream>
#include<string>

using namespace std;

int main(){
	fstream fin,fout,fout2;
	string line;
	int i = 0;
	
	fin.open("dummy.txt", ios::in);
	fout.open("encrypted.txt", ios::out);
	fout2.open("original.txt", ios::out);
	
	while(!fin.eof()){
		getline(fin,line);
	}
	
	while(fout2){
		fout2<<line;
		break;
	}
	
	while(1){
		if(line[i] == '\0'){
			break;
		}
		else{
			line[i] = line[i] + 6;
			i++;
		}
	}

	cout<<line<<endl;
	
	while(fout){
		fout<<line;
		break;
	}
	
	fout.close();
	fin.close();
	return 0;
	
}
