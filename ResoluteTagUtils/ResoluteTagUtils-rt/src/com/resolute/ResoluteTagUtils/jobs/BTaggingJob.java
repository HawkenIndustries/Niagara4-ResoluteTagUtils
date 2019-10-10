package com.resolute.ResoluteTagUtils.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.resolute.ResoluteTagUtils.components.Point;

import javax.baja.file.BIFile;
import javax.baja.job.BSimpleJob;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.naming.UnresolvedException;
import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import javax.baja.tag.TagDictionary;
import javax.baja.tag.TagInfo;
import javax.baja.tagdictionary.BTagDictionaryService;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

@NiagaraType


@NiagaraProperty(
        name = "importFile",
        type = "baja:Ord",
        defaultValue = "BOrd.make(\"file:^ResoluteImports/tagImport.json\")",
        facets = {
                @Facet(name = "BFacets.TARGET_TYPE", value = "\"baja:IFile\"")
        },
        flags = Flags.OPERATOR | Flags.SUMMARY
)
@NiagaraProperty(
        name = "tags",
        type = "baja:String",
        defaultValue = "BString.make(\"\")",
        facets = { @Facet(name = "BFacets.MULTI_LINE", value = "true") },
        flags = Flags.READONLY
)
@NiagaraProperty(
        name = "jsonVersion",
        type = "baja:Double",
        defaultValue = "BDouble.make(0.0)"
)

public class BTaggingJob extends BSimpleJob {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.jobs.BTaggingJob(3465166768)1.0$ @*/
/* Generated Thu Oct 10 16:38:43 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "importFile"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code importFile} property.
   * @see #getImportFile
   * @see #setImportFile
   */
  public static final Property importFile = newProperty(Flags.OPERATOR | Flags.SUMMARY, BOrd.make("file:^ResoluteImports/tagImport.json"), BFacets.make(BFacets.TARGET_TYPE, "baja:IFile"));
  
  /**
   * Get the {@code importFile} property.
   * @see #importFile
   */
  public BOrd getImportFile() { return (BOrd)get(importFile); }
  
  /**
   * Set the {@code importFile} property.
   * @see #importFile
   */
  public void setImportFile(BOrd v) { set(importFile, v, null); }

////////////////////////////////////////////////////////////////
// Property "tags"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code tags} property.
   * @see #getTags
   * @see #setTags
   */
  public static final Property tags = newProperty(Flags.READONLY, BString.make(""), BFacets.make(BFacets.MULTI_LINE, true));
  
  /**
   * Get the {@code tags} property.
   * @see #tags
   */
  public String getTags() { return getString(tags); }
  
  /**
   * Set the {@code tags} property.
   * @see #tags
   */
  public void setTags(String v) { setString(tags, v, null); }

////////////////////////////////////////////////////////////////
// Property "jsonVersion"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code jsonVersion} property.
   * @see #getJsonVersion
   * @see #setJsonVersion
   */
  public static final Property jsonVersion = newProperty(0, ((BDouble)(BDouble.make(0.0))).getDouble(), null);
  
  /**
   * Get the {@code jsonVersion} property.
   * @see #jsonVersion
   */
  public double getJsonVersion() { return getDouble(jsonVersion); }
  
  /**
   * Set the {@code jsonVersion} property.
   * @see #jsonVersion
   */
  public void setJsonVersion(double v) { setDouble(jsonVersion, v, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BTaggingJob.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static Logger logger = Logger.getLogger("Resolute Tag Utils");
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    @Override
    public void run(Context cx){
        try{
            BTagDictionaryService tagDictionaryService = (BTagDictionaryService) this.
                    getParentComponent().get("TagDictionaryService");

            /***
             * Tag Dictionary Service has to be operating properly
             * or this action is a waste of resource.
             */
            if( tagDictionaryService.completesStarted() &&
                    tagDictionaryService.isRunning()    &&
                    (!tagDictionaryService.isDisabled())
            ){
                log().message("Tag Dictionary Service is running");
                logger.finer("Tag Dictionary Service is running...");

                HashSet<Point> points = AccessController.doPrivileged(
                        (PrivilegedAction<HashSet<Point>>)() -> {
                            try {
                                JsonObject json = gson.fromJson( new String(
                                        ((BIFile)getImportFile().get(this))
                                                .read()) , JsonObject.class);

                                /***
                                 * pass primitives right to parent component as properties.
                                 */
                                setJsonVersion(json.get("version").getAsDouble());
                                JsonArray currentTagSet = json.get("currentTagHashSet").getAsJsonArray();
                                StringBuilder sb = new StringBuilder();
                                currentTagSet.forEach( tag -> {
                                    sb.append(tag);
                                });
                                setTags(sb.toString());

                                /***
                                 *Re-serialize the remaining points list object back into a literal string,
                                 * in order to parse it with a matching collection of POJOs
                                 */
                                String jsonPoints = gson.toJson(json.get("points").getAsJsonObject());

                                java.lang.reflect.Type ResolutePointList =
                                        new TypeToken<HashSet<Point>>(){}.getType();

                                return gson.fromJson(jsonPoints , ResolutePointList);

                            } catch (IOException ioe) {
                                logger.severe(ioe.getMessage());
                                ioe.printStackTrace();
                            }catch(Exception e){
                                logger.severe(e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        });

                /***
                 * Iterate over all existing dictionaries and add tags as required
                 */
                Collection<TagDictionary> tagDictionaries = tagDictionaryService.getTagDictionaries();
                int nOfDicts = tagDictionaries.size();

                /***
                 * On-Demand implementation of the functional interface multvsearch.
                 * Iterates over all tags in every dictionary, checking every rbi tag
                 * for every rbi point in the list.
                 */
                multvsearch searcher = (TagInfo tagInfo, HashSet<Point> pointList) -> {
                    log().message("dictionary tag: ".concat(tagInfo.getName()));
                    pointList.forEach(point -> {
                        log().message("Resolute Point: ".concat(point.getName()));
                        for(String tag:point.getTags()){
                            log().message("Resolute Tag: ".concat(tag));
                            if(tagInfo.getName().equals(tag)){
                                try{
                                    log().message("\t\tFound Matching Tag...!!!");
                                    String relPath = "slot:".concat(
                                            (point.getMetricId().split("\\."))[1] );
                                    BOrd ord = BOrd.make(SlotPath.escape(relPath));
                                    //TODO.. you're here...TEST THIS METHOD FOR ADDING TAGS TO BCOMPONENTS...!!!
                                    tagInfo.setTagOn((BComponent) ord.get(Sys.getStation()));

                                }catch(UnresolvedException ue){
                                    log().message(ue.getMessage());
                                    logger.severe(ue.getMessage());
                                }
                            }
                        }
                    });
                };

                log().message("Dictionaries found: ");
                for (TagDictionary tagDictionary : tagDictionaries) {
                    final String msg = "dictionary ".concat(String.valueOf(nOfDicts))
                            + ": " + tagDictionary;
                    log().message(msg);
                    logger.finer(msg);

                    Iterator<TagInfo> tagInfoIterator = tagDictionary.getTags();
                    tagInfoIterator.forEachRemaining(tagInfo -> {
                        if(points != null){
                            if(!points.isEmpty()){
                                searcher.search(tagInfo, points);
                            }else{
                                log().message("Parsed empty list out of json file...");
                                logger.severe("Parsed empty list out of json file...!!!");
                            }
                        }else{
                            log().message("Parsed a null list out of json file...!!!");
                            logger.severe("Parsed a null list out of json file...!!!");
                        }
                    });
                }
            }else{
                Exception tagDictServiceException =
                        new Exception("TagImporter error - TagDictionaryService is not operating as required...");
                logger.severe(tagDictServiceException.getMessage());
                throw tagDictServiceException;
            }
        }catch(Exception e){
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
    }


    /***
     * MULTI-VALUE SEARCH:
     *
     * When iterating over a 'large' collection of objects in search of equality matches for a 'control' collection of
     * objects, after sorting; while implementing a binary search, we must check every item of the large collection
     * against every object in the control collection and if while in search of an item we find any other in the
     * control collection we index it to a hashmap, and continue the binary search until the item being looked for
     * is found.
     * Once an item is found, we do a hashmap lookup for the next item to see if it's indexed, if not we run the
     * above process again, and subsequently the hashmap lookup again as well until all items of the large collection
     * have been compared.
     *
     * A multi-threaded implementation would have the main thread spun one thread per os core available, pass a copy of
     * the control collection to each thread, and have each thread indexing while the main thread searches for matches.
     *
     */
    @FunctionalInterface
    interface multvsearch { void search(TagInfo tagInfo, HashSet<Point> points); }
}
