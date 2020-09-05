#include<iostream>
#include<stdlib.h>
#include<cstring>
#include<time.h>

using namespace std;

//global variables
int hash[16];
char sig[16];

class Hash{
	public:
		
		void giveHash(int shift){
			for(int i=0;i<shift;i++){
					hash[i] = rand()%10;		
			}			
		}
	
		void giveSignature(){
			char alphabet[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 
          				  'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
						  'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
						  'x', 'y', 'z' , '1' , '2' , '3' , '4' , 
						  '5' , '6' , '7' , '8' , '9' , '10' }; 
            for(int i=0; i<16; i++){
            	sig[i]+=alphabet[rand()%36];
			}
		}
};

int main(){
	srand(time(0));
	Hash object;
	object.giveHash(16);
	object.giveSignature();
	cout<<"\nPrivate Signature: "<<sig;
	cout<<"\nGenerated Hash: ";
	for(int i=0;i<16;i++){
		cout<<hash[i];
	}
	return 0;
}
