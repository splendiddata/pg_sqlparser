/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.Severity;
import com.splendiddata.sqlparser.structure.ErrCode;
import com.splendiddata.sqlparser.structure.ErrDetail;
import com.splendiddata.sqlparser.structure.ErrHint;

/**
 * Container for error data to report
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement
public class SqlParserErrorData {
    private Long errorOffset;
    private String errorText;
    private Severity severity;
    private ErrCode errorCode;
    private String errorHint;
    private String errorDetail;

    /**
     * Constructor
     */
    public SqlParserErrorData() {
        // empty
    }

    /**
     * @return Long the errorOffset
     */
    @XmlAttribute
    public final Long getErrorOffset() {
        return errorOffset;
    }

    /**
     * @param errorOffset
     *            the errorOffset to set
     * @param <T>
     *            Allows for subclassing
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final <T extends SqlParserErrorData> T setErrorOffset(Long errorOffset) {
        this.errorOffset = errorOffset;
        return (T) this;
    }

    /**
     * @return String the errorText
     */
    @XmlAttribute
    public final String getErrorText() {
        return errorText;
    }

    /**
     * @return Severity the severity
     */
    @XmlAttribute
    public final Severity getSeverity() {
        return severity;
    }

    /**
     * @param severity
     *            the severity to set
     * @param <T>
     *            Allows for subclassing
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final <T extends SqlParserErrorData> T setSeverity(Severity severity) {
        this.severity = severity;
        return (T) this;
    }

    /**
     * @param errorText
     *            the errorText to set
     * @param <T>
     *            Allows for subclassing
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final <T extends SqlParserErrorData> T setErrorText(String errorText) {
        this.errorText = errorText;
        return (T) this;
    }

    /**
     * @return ErrCode the errorCode
     */
    @XmlAttribute
    public final ErrCode getErrorCode() {
        return errorCode;
    }

    /**
     * @return String the errorHint
     */
    @XmlElement
    public final String getErrorHint() {
        return errorHint;
    }

    /**
     * @param errorHint
     *            the errorHint to set
     * @param <T>
     *            Allows for subclassing
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final <T extends SqlParserErrorData> T setErrorHint(String errorHint) {
        this.errorHint = errorHint;
        return (T) this;
    }

    /**
     * @param errorHint
     *            the errorHint to set
     */
    public final void setErrorHint(ErrHint errorHint) {
        this.errorHint = errorHint.toString();
    }

    /**
     * @param errorCode
     *            the errorCode to set
     * @param <T>
     *            Allows for subclassing
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final <T extends SqlParserErrorData> T setErrorCode(ErrCode errorCode) {
        this.errorCode = errorCode;
        return (T) this;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName()).append(":").append(System.lineSeparator());
        if (severity != null) {
            result.append("    severity = ").append(severity).append(System.lineSeparator());
        }
        if (errorCode != null) {
            result.append("    errorCode = ").append(errorCode).append(System.lineSeparator());
        }
        if (errorText != null) {
            result.append("    errorText = ").append(errorText).append(System.lineSeparator());
        }
        if (errorOffset != null) {
            result.append("    errorOffset = ").append(errorOffset).append(System.lineSeparator());
        }
        if (errorHint != null) {
            result.append("    errorHint = ").append(errorHint).append(System.lineSeparator());
        }
        if (errorDetail != null) {
            result.append("    errorDetail = ").append(errorDetail).append(System.lineSeparator());
        }
        return result.toString();
    }

    /**
     * @return String the errorDetail
     */
    public String getErrorDetail() {
        return errorDetail;
    }

    /**
     * @param errorDetail
     *            the errorDetail to set
     */
    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    /**
     * @param errorDetail
     *            the errorDetail to set
     */
    public void setErrorDetail(ErrDetail errorDetail) {
        this.errorDetail = errorDetail.toString();
    }
}
