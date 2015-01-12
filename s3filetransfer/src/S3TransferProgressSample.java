/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.awt.BorderLayout;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;


/**
 * Demonstrates how to upload data to Amazon S3, and track progress, using a
 * Swing progress bar.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on Amazon
 * S3, see http://aws.amazon.com/s3.
 * <p>
 * WANRNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 *
 * http://aws.amazon.com/security-credentials
 */
public class S3TransferProgressSample {

    private static AWSCredentials credentials = null;
    private static TransferManager tx;
    private static String bucketName;

    private JProgressBar pb;
    private JFrame frame;
    private static Upload upload;
    private JButton button;
    private static long Oregonbucketsize = 5120000;
    private static long northcaliforniasize = 5120000;
    private static long singaporesize = 5120000;
    private static long tokyosize = 5120000;
    private static long sydneysize = 5120000;

    public static void main(String[] args) throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (/home/sravya/.aws/credentials).
         *
         * TransferManager manages a pool of threads, so we create a
         * single instance and share it throughout our application.
         */
       try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/home/sravya/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = new AmazonS3Client(credentials);
        
       // String regions[] = {"US_WEST_2","US_WEST_1","AP_SOUTHEAST_1","AP_NORTHEAST_1","AP_SOUTHEAST_2"};
        /*Region reg = null;
       

        for( int i = 0; i < 5; i++)
        {
           // String region = regions[i];
            //String nextElement = regions[i+1];
        if(i==0)
        {
        reg = Region.getRegion(Regions.US_WEST_2);
        }
        else if(i==1)
        {
        	reg = Region.getRegion(Regions.US_WEST_1);	
        }
        else if(i==1)
        {
        	reg = Region.getRegion(Regions.AP_SOUTHEAST_1);	
        }
        else if(i==1)
        {
        	reg = Region.getRegion(Regions.AP_NORTHEAST_1);	
        }
        else if(i==1)
        {
        	reg = Region.getRegion(Regions.AP_SOUTHEAST_2);	
        }*/
        Region reg = Region.getRegion(Regions.US_WEST_2);
       
        s3.setRegion(reg);
        tx = new TransferManager(s3);
        System.out.println("List of IDCs");
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
               
                bucketName= bucket.getName();
                    AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
                    //try {
                    
                        System.out.println(bucketName);
               
                        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                            .withBucketName(bucketName);
                            
                        ObjectListing objectListing;            
                        do {
                            objectListing = s3client.listObjects(listObjectsRequest);
                            
                            long size= 0;
                            /*Oregonbucketsize = 5120000;
                            northcaliforniasize = 5120000;
                            singaporesize = 5120000;
                            tokyosize = 5120000;
                            sydneysize = 5120000;*/
                            
                            	 if(bucketName.equals("projectidc1"))
                                 {   size = 0;
                            		 for (S3ObjectSummary objectSummary : 
                                     	objectListing.getObjectSummaries()) {
                                     	 size= size + objectSummary.getSize();
                            		
                            		 
                            		 }
                            		 Oregonbucketsize = 5120000 - size;
                            		 if(Oregonbucketsize < 15216)
                            		 {
                            			 Oregonbucketsize = 0; 
                            		 }
                            		 
                                 }
                            	 else if(bucketName.equals("projectidc2"))
                            	 {
                            		 size = 0;
                            		 for (S3ObjectSummary objectSummary : 
                                     	objectListing.getObjectSummaries()) {
                                     	 size= size + objectSummary.getSize();
                            		
                                     	
                            		 }
                            		 northcaliforniasize = 5120000 - size;
                            		
                            		 if(northcaliforniasize < 15216)
                            		 {
                            			 northcaliforniasize = 0; 
                            		 }
                            		 
                            	 }
                            	 else if(bucketName.equals("projectidc3"))
                            	 {   
                            		 size = 0;
                            		 for (S3ObjectSummary objectSummary : 
                                     	objectListing.getObjectSummaries()) {
                                     	 size= size + objectSummary.getSize();
                            		
                                     	
                            		 }
                            		 singaporesize = 5120000 - size;
                            		 if(singaporesize < 15216)
                            		 {
                            			 singaporesize = 0; 
                            		 }
                            		
                            	 }
                            	 else if(bucketName.equals("projectidc4"))
                            	 {
                            		 size = 0;
                            		 for (S3ObjectSummary objectSummary : 
                                     	objectListing.getObjectSummaries()) {
                                     	 size= size + objectSummary.getSize();
                            		
                                     	
                            		 }
                            		 tokyosize = 5120000 - size;
                            		 if(tokyosize < 15216)
                            		 {
                            			 tokyosize = 0; 
                            		 }
                            		 
                            	 }
                            	 else if(bucketName.equals("projectidc5"))
                            	 {
                            		 size = 0;
                            		 for (S3ObjectSummary objectSummary : 
                                     	objectListing.getObjectSummaries()) {
                                     	 size= size + objectSummary.getSize();
                            		
                                     	
                            		 }
                            		 sydneysize = 5120000 - size;
                            		 if(sydneysize < 15216)
                            		 {
                            			 sydneysize = 0; 
                            		 }
                            		
                            		 
                            	 }
                            	 
                            	 
                            	// System.out.println("Oregonbucketsize  =" + Oregonbucketsize);
                               /* System.out.println(" - " + objectSummary.getKey() + "  " +
                                        "(size = " + objectSummary.getSize() + 
                                        ")");
                                System.out.println("Total size =" + size);*/
                            
                            listObjectsRequest.setMarker(objectListing.getNextMarker());
                            
                            
                        } while (objectListing.isTruncated());
                    }
        
        if(Oregonbucketsize == 0)
        {
        	
        	Oregonbucketsize = 5120000;
            
            
            
            
        }
        if(northcaliforniasize == 0)
        {
        	northcaliforniasize = 5120000;
        }
        else if(singaporesize == 0)
        {
        	singaporesize = 5120000;
        }
        else if(tokyosize== 0)
        {
        	tokyosize = 5120000;
        }
        else if(sydneysize == 0)
        {
        	sydneysize = 5120000;
        }
        
        
        System.out.println("Oregonbucketsize  =" + Oregonbucketsize);
        System.out.println("Northcalifornia  =" + northcaliforniasize);
        System.out.println("singapore  =" + singaporesize);
        System.out.println("tokyo  =" + tokyosize);
        System.out.println("sydney  =" + sydneysize);
        PrintWriter writer = new PrintWriter("/home/sravya/location/in.alg", "UTF-8");
        writer.println(Oregonbucketsize);
        writer.println(northcaliforniasize);
        writer.println(singaporesize);
        writer.println(tokyosize);
        writer.println(sydneysize);
        writer.close();
        
    
       
       // bucketName = "projectidc1";
        
        try{
            
            String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
           // BufferedWriter out = new BufferedWriter(new FileWriter("/home/sravya/mypython.py"));
           // out.write(prg);
           // out.close();
           // int number1 = 8;
          // int number2 = 32;
          //  Process p = Runtime.getRuntime().exec("python test1.py "+number1+" "+number2);
            Process p = Runtime.getRuntime().exec("python /home/sravya/location/ramd.py ");
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
           // System.out.println("sydney  =" + sydneysize);
            File log = new File("/home/sravya/location/work.alg");
           // PrintWriter out = new PrintWriter(new FileWriter(log, true));
           // out.close();
            //BufferedReader in = ne;w BufferedReader(new InputStreamReader(p.getInputStream()));
            //int ret = new Integer(in.readLine()).intValue();
            //System.out.println("value is : "+ret);
            int filenumber=0;
            Scanner numberscan = new Scanner(new File("/home/sravya/location/filenumber.alg"));
            if(numberscan.hasNextLine())
            {
            	filenumber = numberscan.nextInt();
            	
            }
            else
            {
            	filenumber = 1;
            	
            }
            numberscan.close();
          //System.out.println("value is : "+filenumber);
            Scanner s = new Scanner(new File("/home/sravya/location/work.alg"));
          
           // int[] array = new int[s.nextInt()];
           // System.out.println("value is : "+array.length);
           // for (int i = 0; i < 5; i++)
            while(s.hasNextInt())
            {   //int[] array = new int[s.nextInt()];
            	int ss = s.nextInt();
            	int i=0;
            	
            int[] array = new int[]{0};
            
            	array[i] = ss;
               
                int graph= array[i];
                while (graph > 0) {
                    //print( graph % 10);
                    int idc = graph % 10;
                    graph = graph / 10;
                    System.out.println("IDC "+ idc);
                    if(idc == 1)
                    {
                    bucketName = "projectidc1";
                    }
                    else if(idc == 2)
                    {
                    	bucketName = "projectidc2";	
                    }
                    else if(idc == 3)
                    {
                    	bucketName = "projectidc3";
                    }
                    else if(idc == 4)
                    {
                    	bucketName = "projectidc4";
                    }
                    else if(idc == 5)
                    {
                    	bucketName = "projectidc5";
                    }
                    
                                    
              
                	String keyName        = "file"+filenumber++ +".jpg";
                	String uploadFileName = "/home/sravya/Downloads/photos/file1.jpg";
                	
                	AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
                        try {
                            System.out.println("Uploading a file to S3\n");
                            File file = new File(uploadFileName);
                            s3client.putObject(new PutObjectRequest(
                            		                 bucketName, keyName, file));
                            
                     
                         } catch (AmazonServiceException ase) {
                            System.out.println("Caught an AmazonServiceException, which " +
                            		"means your request made it " +
                                    "to Amazon S3, but was rejected with an error response" +
                                    " for some reason.");
                            System.out.println("Error Message:    " + ase.getMessage());
                            System.out.println("HTTP Status Code: " + ase.getStatusCode());
                            System.out.println("AWS Error Code:   " + ase.getErrorCode());
                            System.out.println("Error Type:       " + ase.getErrorType());
                            System.out.println("Request ID:       " + ase.getRequestId());
                        } 
                    
                    
                    
                    
                    
                }
                
                i++;
            }
            s.close();
            PrintWriter numberwriter = new PrintWriter("/home/sravya/location/filenumber.alg", "UTF-8");
            numberwriter.println(filenumber);
            numberwriter.close();
            }catch(Exception e){}
    }

        //new S3TransferProgressSample();
   

    public S3TransferProgressSample() throws Exception {
        frame = new JFrame("Amazon S3 File Upload");
        button = new JButton("Choose File...");
        button.addActionListener(new ButtonListener());

        pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);

        frame.setContentPane(createContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            int showOpenDialog = fileChooser.showOpenDialog(frame);
            if (showOpenDialog != JFileChooser.APPROVE_OPTION) return;

            //createAmazonS3Bucket();

            ProgressListener progressListener = new ProgressListener() {
                public void progressChanged(ProgressEvent progressEvent) {
                    if (upload == null) return;

                    pb.setValue((int)upload.getProgress().getPercentTransferred());

                    switch (progressEvent.getEventCode()) {
                    case ProgressEvent.COMPLETED_EVENT_CODE:
                        pb.setValue(100);
                        break;
                    case ProgressEvent.FAILED_EVENT_CODE:
                        try {
                            AmazonClientException e = upload.waitForException();
                            JOptionPane.showMessageDialog(frame,
                                    "Unable to upload file to Amazon S3: " + e.getMessage(),
                                    "Error Uploading File", JOptionPane.ERROR_MESSAGE);
                        } catch (InterruptedException e) {}
                        break;
                    }
                }
            };

            File fileToUpload = fileChooser.getSelectedFile();
            PutObjectRequest request = new PutObjectRequest(
                    bucketName, fileToUpload.getName(), fileToUpload)
                .withGeneralProgressListener(progressListener);
            upload = tx.upload(request);
        }
    }

    private void createAmazonS3Bucket() {
        try {
            if (tx.getAmazonS3Client().doesBucketExist(bucketName) == false) {
                tx.getAmazonS3Client().createBucket(bucketName);
            }
        } catch (AmazonClientException ace) {
            JOptionPane.showMessageDialog(frame, "Unable to create a new Amazon S3 bucket: " + ace.getMessage(),
                    "Error Creating Bucket", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createContentPane() {
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(pb);

        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.add(panel, BorderLayout.NORTH);
        borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return borderPanel;
    }
}
