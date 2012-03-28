/**
 * Copyright (c) 2011 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Jan 10, 2011
 */
package jndi.view;

import static java.util.Collections.emptyList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jndi.view.utils.Reflection;

import org.springframework.jndi.JndiCallback;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Alistair A. Israel
 */
@Controller
@RequestMapping("*")
public class JndiView extends ParameterizableViewController {

    private static final Logger logger = Logger.getLogger(JndiView.class.getCanonicalName());

    /**
     * {@value #JAVA_GLOBAL}
     */
    public static final String JAVA_GLOBAL = "java:global";

    private final JndiTemplate jndiTemplate = new JndiTemplate();

    /**
     *
     */
    public JndiView() {
        setViewName("jndi");
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final ModelAndView handleRequestInternal(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ModelAndView mav = new ModelAndView(getViewName());

        final String uri = request.getRequestURI();
        logger.finest("uri: \"" + uri + "\"");

        final String prefix = request.getContextPath();
        mav.addObject("prefix", prefix);

        request.getContextPath();
        String path;
        if (uri.startsWith(prefix)) {
            path = uri.substring(prefix.length());
        } else {
            path = uri;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        logger.finer("path: \"" + path + "\"");
        mav.addObject("path", path);

        mav.addObject("entries", browse(path));
        if (path.isEmpty()) {
            mav.addObject("displayPath", "/");
        } else {
            mav.addObject("displayPath", path);
        }
        mav.addObject("now", new Date());
        return mav;
    }

    /**
     * @param path
     *        the path to browse
     * @return {@link List} of {@link JndiEntry}s
     * @throws NamingException
     *         on exception
     */
    private List<JndiEntry> browse(final String path) throws NamingException {
        final JndiCallback<List<JndiEntry>> contextCallback = new JndiCallback<List<JndiEntry>>() {
            @Override
            public List<JndiEntry> doInContext(final Context context) throws NamingException {
                if (JAVA_GLOBAL.equals(path)) {
                    // Do a little trick to handle "java:global"
                    final NamingEnumeration<Binding> root = context.listBindings("");
                    Context javaGlobalContext = null;
                    while (root.hasMore()) {
                        final Binding binding = root.next();
                        if (JAVA_GLOBAL.equals(binding.getName())) {
                            final Object obj = binding.getObject();
                            if (obj instanceof Context) {
                                javaGlobalContext = (Context) obj;
                            }
                            break;
                        }
                    }
                    if (javaGlobalContext != null) {
                        return examineBindings(javaGlobalContext, path, javaGlobalContext.listBindings(""));
                    }
                    logger.warning("Unable to browse \"" + JAVA_GLOBAL + "\" namespace!");
                    return emptyList();
                }
                return examineBindings(context, path, context.listBindings(path));
            }
        };
        return jndiTemplate.execute(contextCallback);
    }

    /**
     * @param ctx
     *        the Context we're examining
     * @param path
     *        the path to examine
     * @param bindings
     *        the {@link NamingEnumeration} of {@link Binding}s
     * @return List of {@link JndiEntry}
     * @throws NamingException
     *         on exception
     */
    private List<JndiEntry> examineBindings(final Context ctx, final String path,
            final NamingEnumeration<Binding> bindings) throws NamingException {
        if (null == bindings) {
            throw new NullPointerException("bindings is null!");
        }
        final List<JndiEntry> entries = new ArrayList<JndiEntry>();
        while (bindings.hasMore()) {
            final Binding binding = bindings.next();
            final String name = binding.getName();
            final String className = binding.getClassName();

            logger.finest("name: " + name + " [" + className + "]");
            final JndiEntry entry = new JndiEntry(name, className);
            final Object obj = binding.getObject();
            if (obj instanceof Context) {
                entry.setContext(true);
                String link = name;
                if (!path.isEmpty()) {
                    link = path + "/" + name;
                }
                entry.setLink(link);
            } else if (obj instanceof Reference) {
                final Reference ref = (Reference) obj;
                entry.setTargetClassName(ref.getClassName());
            } else if ("org.glassfish.javaee.services.ResourceProxy".equals(className)) {
                // SUPPRESS CHECKSTYLE AvoidInlineConditionals
                final Object lookup = ctx.lookup(path.isEmpty() ? name : path + "/" + name);
                if (lookup != null) {
                    final String lookedUpClassName = lookup.getClass().getName();
                    logger.finest("lookup(\"" + name + "\") returned " + lookedUpClassName);
                    entry.setTargetClassName(lookedUpClassName);
                }
            } else if ("com.sun.ejb.containers.JavaGlobalJndiNamingObjectProxy".equals(className)) {
                inspectJndiNamingObjectProxy(entry, obj);
            }
            entries.add(entry);
        }
        return entries;
    }

    /**
     * @param entry
     *        the {@link JndiEntry} we're working on
     * @param obj
     *        the Object we're inspecting
     */
    private void inspectJndiNamingObjectProxy(final JndiEntry entry, final Object obj) {
        final Field f = ReflectionUtils.findField(obj.getClass(), "intfName");
        if (f != null) {
            final Object v = Reflection.getField(obj, f);
            logger.finest("intfName: " + v);
            entry.setTargetClassName(v.toString());
        }
    }
}
