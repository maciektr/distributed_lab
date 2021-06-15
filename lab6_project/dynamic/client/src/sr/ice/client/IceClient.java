package sr.ice.client;
// **********************************************************************
//
// Copyright (c) 2003-2019 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

import Demo.*;

import java.lang.Exception;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import com.zeroc.Ice.*;

public class IceClient {
	public void callAdd(int a, int b){
		OutputStream out = new OutputStream(communicator);
		out.startEncapsulation();
		out.writeInt(a);
		out.writeInt(b);
		out.endEncapsulation();
		byte[] inParams = out.finished();

		com.zeroc.Ice.Object.Ice_invokeResult r = base1.ice_invoke("add", com.zeroc.Ice.OperationMode.Normal, inParams);
		if(r.returnValue)
		{
			com.zeroc.Ice.InputStream in = new com.zeroc.Ice.InputStream(communicator, r.outParams);
			in.startEncapsulation();
			int result = in.readInt();
			in.endEncapsulation();
			System.out.println(a + " + " + b + " = " + result);
		}
		else
		{
		}
	}

	public void callSubtract(int a, int b){
		OutputStream out = new OutputStream(communicator);
		out.startEncapsulation();
		out.writeInt(a);
		out.writeInt(b);
		out.endEncapsulation();
		byte[] inParams = out.finished();

		com.zeroc.Ice.Object.Ice_invokeResult r = base1.ice_invoke("subtract", com.zeroc.Ice.OperationMode.Normal, inParams);
		if(r.returnValue)
		{
			com.zeroc.Ice.InputStream in = new com.zeroc.Ice.InputStream(communicator, r.outParams);
			in.startEncapsulation();
			int result = in.readInt();
			in.endEncapsulation();
			System.out.println(a + " - " + b + " = " + result);
		}
		else
		{
		}
	}

	public void callOp(A a, int b){
		OutputStream out = new OutputStream(communicator);
		out.startEncapsulation();
		A.ice_write(out, a);
		out.writeInt(b);
		out.endEncapsulation();
		byte[] inParams = out.finished();

		com.zeroc.Ice.Object.Ice_invokeResult r = base1.ice_invoke("op", com.zeroc.Ice.OperationMode.Normal, inParams);
		if(r.returnValue)
		{
			com.zeroc.Ice.InputStream in = new com.zeroc.Ice.InputStream(communicator, r.outParams);
			in.startEncapsulation();
			String result = in.readString();
			in.endEncapsulation();
			System.out.println("op(" +
				" A { a = "+ a.a + ", b = " + a.b + ", c = " + a.c + ", d = " + a.d + "}"
				+ " , " + b + ") = " + result);
		}
		else
		{
		}
	}

	Communicator communicator = null;
	ObjectPrx base1 = null;

	public IceClient(String[] args){
			communicator = Util.initialize(args);
			base1 = communicator.stringToProxy("DynamicObject : tcp -h 127.0.0.2 -p 10000 -z -t 10000 : udp -h 127.0.0.2 -p 10000 -z");
	}


	public static void main(String[] args)
	{
		int status = 0;
		IceClient client = new IceClient(args);
		try {
			CompletableFuture<Long> cfl = null;
			String line = null;
			client.callAdd(1,2);
			client.callSubtract(10, 5);
			client.callOp(new A((short)11, 22, 33.0f, "Ala ma kota Ala ma kota Ala ma kota  kota Ala ma kota Ala ma kota "), 12);

			do {
				Scanner scan = new Scanner(System.in);
				System.out.println("==> ");
				line = scan.nextLine();
				if (line == null) {
					break;
				} else if (line.equals("add") || line.equals("subtract")){
					int a = scan.nextInt();
					int b = scan.nextInt();
					if (line.equals("add"))
						client.callAdd(a,b);
					else
						client.callSubtract(a,b);
				} else if(line.equals("op")){
					String d = scan.nextLine();
					A a = new A((short)11, 22, 33.0f, d);
					int b = scan.nextInt();
					client.callOp(a,b);
				}
			}
			while (!line.equals("x"));


		} catch (LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (client.communicator != null) {
			try {
				client.communicator.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}
}