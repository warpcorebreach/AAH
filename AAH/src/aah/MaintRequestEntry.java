/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.sql.Date;

/**
 *
 * @author Weiyu
 */
public class MaintRequestEntry {
    private final Date requestDate;
    private final int aptno;
    private final String issue;
    private final Date resolvedDate;
    public MaintRequestEntry(Date requestDate, int aptno, String issue, Date resolvedDate) {
        this.requestDate = requestDate;
        this.aptno = aptno;
        this.issue = issue;
        this.resolvedDate = resolvedDate;
    }
    public Date getrequestDate() {
        return requestDate;
    }
    public int getaptNo() {
        return aptno;
    }
    public String getissue() {
        return issue;
    }
    public Date getResolvedDate() {
        return resolvedDate;
    }
}
