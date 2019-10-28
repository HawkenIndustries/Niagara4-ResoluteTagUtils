/***
 * 10/27/2019
 * Victor Smolinski
 *
 * It allows a Niagara user to remove direct tags from Niagara components in bulk using a csv string as a
 * filter, which can be used to filter by tag dictionary or by tag name as comma separated values,
 * if all tags from all dictionaries need to be deleted then just enter '*'.
 */

package com.resolute.ResoluteTagUtils.jobs;

import com.resolute.ResoluteTagUtils.components.BTagImporter;
import com.resolute.ResoluteTagUtils.services.BResoluteTagUtils;

import javax.baja.collection.BITable;
import javax.baja.collection.TableCursor;
import javax.baja.job.BSimpleJob;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import javax.baja.tag.TagDictionary;
import javax.baja.tag.TagDictionaryService;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@NiagaraType

public class BBulkTagRemoveJob extends BSimpleJob {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.jobs.BBulkTagRemoveJob(2979906276)1.0$ @*/
/* Generated Tue Oct 15 10:14:09 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

    /***
     * Filter semantics:
     *      "*" = remove all tags from all points and all tag-dictionary (Exception: Implied tags and smart tags will remain)
     *      ""  = will produce a null pointer runtime exception
     *      "abc,abc,abc" = will produce an array of resolute tags to match and remove from all points and all matching
     *      dictionaries.
     */
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BBulkTagRemoveJob.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static Logger logger = Logger.getLogger("Resolute Tag Utils");

    @Override
    public void run(Context context) throws Exception {

        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

        setProgress(0);
        progress(0);
        logger.info("Bulk-Tag-Remove-Job Progress...".concat(String.valueOf(getProgress())));

        HashSet<BComponent> points;
        if(tagImporter.getUseScope()){
            points = resolve (tagImporter.getScopeOfWork());
        }else{
            points = resolve (BOrd.make("slot:|bql:select * from control:ControlPoint"));
        }

        TagDictionaryService tagDictionaryService = Sys.getStation().getTagDictionaryService();
        Collection<TagDictionary> tagDictionaries = tagDictionaryService.getTagDictionaries();

        if(tagImporter.getDeleteFilter().isEmpty()){
            logger.severe("[Bulk Tag Remove Async Job ERROR] - tagFilter field is empty; canceling job...!");
            this.cancel();
        }else{
            String[] filters = tagImporter.getDeleteFilter().split(",");
            setProgress(1);
            progress(1);
            logger.info("Bulk-Tag-Remove-Job Progress...".concat(String.valueOf(getProgress())));
            points.forEach( point -> {
                if(filters[0].equals("*")){

                    final long before, after, elapsed;
                    AtomicReference<Integer> totalOps = new AtomicReference<>(0);
                    long progressUnitPerPoint = 100 / points.size();
                    before = System.currentTimeMillis();
                    point.tags().forEach( tag -> {


                        totalOps.set(totalOps.get() + point.tags().getAll().size());
                        long progressUnit = progressUnitPerPoint / point.tags().getAll().size();

                        if(getProgress() < 100){
                            int p = getProgress();
                            setProgress(p+=progressUnit);
                            progress(p+=progressUnit);
                        }

                        boolean removed = point.tags().remove(tag);
                        String msg = "Removed ".concat(tag.getId().getQName()) +
                                " - ".concat(String.valueOf(removed));
                        log().message(msg);
                        logger.info(msg);
                    });
                     after = System.currentTimeMillis();
                     elapsed = after - before;
                     logger.info("Point: ".concat(point.getName()));
                     logger.info("Start Time: ".concat(String.valueOf(before)));
                     logger.info("End Time: ".concat(String.valueOf(after)));
                     logger.info("------------");
                     logger.info("Elapsed Time: ".concat(String.valueOf(elapsed)));
                     logger.info("Total Ops: ".concat(String.valueOf(totalOps.get())));
                     logger.info("Time elapsed Per Operation: "
                            .concat(String.valueOf((double) elapsed / (double)totalOps.get())) + " millis");

                }else{
                    filterDictionaries(filters, tagDictionaries, point);
                    filterTags(filters, point);
                }
            });
        }
    }

    /***
     * The member resolve takes a bql query as an argument to scope out the points the operation will use as
     * its data set. It returns an ArrayList of BComponents resolved from the query.
     * @param ord
     * @return
     */
    @SuppressWarnings("unchecked")
    public HashSet<BComponent> resolve(BOrd ord){
        BITable<BComponent> t = (BITable)ord.resolve(Sys.getStation()).get();
        HashSet<BComponent> objRefs = new HashSet<>(100, 100);
        TableCursor<BComponent> c = t.cursor();
        while(c.next()){
            try{
                objRefs.add(c.get());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return objRefs;
    }

    /***
     * Pass this method to a consumer action operating on a collection of niagara components, use it to
     * filter through the components by tag and remove the selected ones.
     * @param filter
     * @param point
     */
    private void filterTags(String[]filter, BComponent point) {
        for(String f : filter){
            point.tags().forEach( tag -> {
                logger.info("tag: ".concat(tag.getId().getName()) +
                        "\t-\tfilter: ".concat(f));
                if(tag.getId().getName().equals(f)){
                    boolean removed = point.tags().remove(tag);
                    String msg = "Removed ".concat(tag.getId().getQName()) +
                            " - ".concat(String.valueOf(removed));
                    log().message(msg);
                    logger.info(msg);
                }
            });
        }
    }

    /***
     * Pass this method to a consumer action operating on a collection of niagara components, use it to
     * filter through the components by tag dictionary and remove the selected ones.
     * @param filter
     * @param tagDictionaries
     * @param point
     */
    private void filterDictionaries(String[] filter,
                                   Collection<TagDictionary> tagDictionaries,
                                   BComponent point) {
        for(String f : filter){
            tagDictionaries.forEach( tagDictionary -> {
                if(tagDictionary.getDisplayName(null).equals(f)){
                    logger.info("Dictionary: ".concat(tagDictionary.getDisplayName(null)) +
                                "\t-\tfilter:  ".concat(f));
                    point.tags().forEach( tag -> {
                        logger.info("Tag's dictionary ".concat(tag.getId().getDictionary()));

                        if(tag.getId().getDictionary()
                                .equals(tagDictionary.getNamespace())){
                            boolean removed = point.tags().remove(tag);

                            logger.info("Id: ".concat(tag.getId().toString()) +
                                    "\nNamespace: ".concat(tagDictionary.getNamespace()) +
                                    "\nRemoved "+removed);
                            log().message("Id: ".concat(tag.getId().toString()) +
                                    "\nNamespace: ".concat(tagDictionary.getNamespace()) +
                                    "\nRemoved "+removed);
                        }
                    });
                }
            });
        }
    }
}
