package vttp2022.assessmockmockv2.server;

import java.io.File;

public class Repository {
  private File repository;
  private String docRootDirectory = "";

  public Repository(String repo){
    System.out.println("Repository - Repo: " + repo);
    System.out.println("Repository - Working Directory = " + System.getProperty("user.dir"));

    // 1. - Default
    //  static

    // 2. - --docRoot got some value
    // ./target:/opt/tmp/www
    // String repoTerms[] - splitting by :
    //    0               1
    // ./target      /opt/tmp/www

    //   0
    // split

    //    0         1
    // ./target /opt/tmp/www

    // /Users/Zhun/Desktop/VTTP/vttp2022_batch2_v2/assesmentmockv2/target/target/opt/tmp/www
    String[] repoTerms = repo.split(":");
    if (repoTerms.length > 1){
      //                              ./target:/opt/tmp/www                 ./target/opt/tmp/www
      this.docRootDirectory = "/" +           repo          .replaceAll(":", "");
    } else{
      // /split
      this.docRootDirectory = "/" + repo;
    }

    //1. /Users/Zhun/Desktop/VTTP/vttp2022_batch2_v2/assesmentmockv2/target/static
    //2. Users/Zhun/Desktop/VTTP/vttp2022_batch2_v2/assesmentmockv2/target/opt/tmp/www
    repo =  System.getProperty("user.dir").concat(this.docRootDirectory);
    System.out.println("New Working Directory = " + repo);
    this.repository = new File(repo);
  }


  public String getAbsolutePath(){
    // System.out.println(this.repository.getAbsolutePath());
    return this.repository.getAbsolutePath();
  }

  public boolean isPathExist(){
    // if (this.repository.exists()){
    //   return true;
    // }else{
    //   return false;
    // }

    return this.repository.exists();
  }

  public boolean isPathDirectory(){
    return this.repository.isDirectory();
  }

  public boolean isPathReadable(){
    return this.repository.canRead();
  }

  public boolean isResouceFound(String resourcePath){
     for (File file : this.repository.listFiles()){
        System.out.println("Repository - isResourceFound: " + file.getName());
        if (resourcePath.substring(1).equals(file.getName())){
          return true;
        }
      }
      return false;
  }
}
