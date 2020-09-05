#include<iostream>
#include<string>
#include<fstream>

using namespace std;

//variable initialization
string phrase,encrypted,decrypted;
int count = 0 , length,flag = 0,ch,shift = 2;
//file stream

	
void menu(){
	cout<<"\n\n--- cryptology menu --- \n\n1.encryption (string) \n2.decryption (encrypted string) \n3.Change Shift \n4.exit";
	cout<<"\n\nenter your choice (1/2): ";
	cin>>ch;
}

string encrypt(string message){
	flag = 1;
	encrypted = phrase;
	length = phrase.length();
	
	for(count; count<length ;count++){
		if(isalpha(phrase[count])){
			if(encrypted[count] == 'z'){
				encrypted[count] = 'a';
			}
			else{
				encrypted[count] = int(encrypted[count]) + shift;
			}
		}
	}
	return encrypted;
}

string decrypt(string message){
	flag = 2;	
	decrypted = phrase;
	
	for(int i=0;i<sizeof(decrypted);i++){
		decrypted[i] = decrypted[i] - shift;
	}
	
	return decrypted;
}

int get_shift(){
	flag = 3;
	cout<<endl<<"enter shift value: ";
	cin>>shift;
}


int main(){
	fstream en,de;
	en.open("C:\\Users\\Methran\\Desktop\\encrypted.txt" , ios::out | ios::app);
	de.open("C:\\Users\\Methran\\Desktop\\decrypted.txt" , ios::out | ios::app);		
	
	
	menu();
	switch(ch){
		case 1:
			cout<<"enter string: ";
			cin>>phrase;
			cout<<endl<<encrypt(phrase);
			break;
		case 2:
			cout<<"enter string: ";
			cin>>phrase;
			cout<<endl<<decrypt(phrase);
			break;
		case 3:
			get_shift();
			break;
		case 4:
			exit(0);
	}
	
	if(flag == 1){
		cout<<endl<<"encrypted data stored in \"encrypted.txt\" [Shift_Value: "<<shift<<"]"<<endl;
		en<<phrase<<" -> "<<encrypted<<" [Shift Value: "<<shift<<"]"<<endl;
	}
	else if(flag == 2){
		cout<<endl<<"decrypted data stored in \"decrypted.txt\" [Shift_Value:"<<shift<<"]"<<endl;
		de<<phrase<<" -> "<<decrypted<<" [Shift Value: "<<shift<<"]"<<endl;
	}
	else if(flag == 3){
		main();
	}
	
	en.close();
	de.close();
	
	return 0;
}
