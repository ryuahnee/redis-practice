package com.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class RedisPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisPracticeApplication.class, args);
		Scanner sc = new Scanner(System.in);
		String s = "1234";

		int result = 0;
		char a = 0;
		int start = 0;
		a = s.charAt(start);
		boolean t = false;
		if(a == '-'){
			t = true;
			start = 1;
			a = s.charAt(start);
		}

		result = Character.getNumericValue(a);
		System.out.println("start result: "+result);

		for(int i = start+1; i < s.length(); i ++){
			a = s.charAt(i);
			System.out.println("char a : "+a);
			result = result * 10  + Character.getNumericValue(a);
			System.out.println("result: "+result);
		}

		System.out.println(t ? -1*result: result);
	}

	//Integer.parseInt()
}//1
// 10+2 =12
// 120+3 =123
//1230 = 4
