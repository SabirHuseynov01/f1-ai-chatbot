// ===================================
// F1 AI Chatbot - Frontend JS
// ===================================

const messagesArea = document.getElementById('messagesArea');
const messageInput = document.getElementById('messageInput');
const sendBtn     = document.getElementById('sendBtn');
const typingIndicator = document.getElementById('typingIndicator');

function getSessionId() {
    return document.getElementById('sessionId').value;
}

// ===========================
// Send Message
// ===========================
async function sendMessage() {
    const text = messageInput.value.trim();
    if (!text) return;

    // UI: disable input, show user message
    setInputDisabled(true);
    appendMessage('user', text);
    messageInput.value = '';
    autoResize(messageInput);

    // Show typing
    showTyping(true);
    scrollToBottom();

    try {
        const response = await fetch('/api/chat/message', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message: text,
                sessionId: getSessionId()
            })
        });

        const data = await response.json();
        showTyping(false);

        if (data.success) {
            appendMessage('bot', data.message);
            // Update session if new one was created
            if (data.sessionId) {
                document.getElementById('sessionId').value = data.sessionId;
            }
        } else {
            appendMessage('bot', '⚠️ ' + (data.error || 'Bir hata oluştu. Lütfen tekrar deneyin.'), true);
        }
    } catch (err) {
        showTyping(false);
        appendMessage('bot', '⚠️ Sunucuya bağlanılamadı. Lütfen sayfayı yenileyin.', true);
        console.error('Chat error:', err);
    } finally {
        setInputDisabled(false);
        messageInput.focus();
        scrollToBottom();
    }
}

// ===========================
// Append message to DOM
// ===========================
function appendMessage(sender, text, isError = false) {
    const div = document.createElement('div');
    div.className = `message ${sender === 'user' ? 'user-message' : 'bot-message'}`;

    const avatar = sender === 'user' ? '👤' : '🤖';
    const formattedText = formatMessageText(text);

    div.innerHTML = `
        <div class="message-avatar">${avatar}</div>
        <div class="message-bubble ${isError ? 'error-bubble' : ''}">${formattedText}</div>
    `;

    messagesArea.appendChild(div);
    scrollToBottom();
}

// ===========================
// Format text: markdown-lite
// ===========================
function formatMessageText(text) {
    // Convert newlines to <br> or <p> tags
    let formatted = text
        // Bold: **text** or __text__
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/__(.*?)__/g, '<strong>$1</strong>')
        // Italic
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        // Inline code
        .replace(/`(.*?)`/g, '<code style="background:rgba(255,255,255,0.1);padding:1px 5px;border-radius:3px;">$1</code>')
        // Headers
        .replace(/^### (.*$)/gm, '<h4 style="color:#e10600;margin:0.5rem 0 0.2rem;">$1</h4>')
        .replace(/^## (.*$)/gm, '<h3 style="color:#e10600;margin:0.5rem 0 0.3rem;">$1</h3>')
        .replace(/^# (.*$)/gm, '<h2 style="color:#e10600;margin:0.5rem 0 0.4rem;">$1</h2>');

    // Paragraphs: split by double newline
    const paragraphs = formatted.split(/\n\n+/);
    if (paragraphs.length > 1) {
        return paragraphs.map(p => {
            // Check if it's a list-like paragraph
            if (p.trim().startsWith('- ') || p.trim().match(/^\d+\./)) {
                const items = p.split('\n').filter(l => l.trim());
                return '<ul style="margin:0.3rem 0 0.3rem 1rem;padding:0;">' +
                    items.map(i => {
                        const content = i.replace(/^[-*]\s+/, '').replace(/^\d+\.\s+/, '');
                        return `<li style="margin:0.2rem 0;">${content}</li>`;
                    }).join('') +
                    '</ul>';
            }
            return p.trim() ? `<p>${p.replace(/\n/g, '<br>')}</p>` : '';
        }).join('');
    }

    // Single paragraph - just handle newlines
    return formatted.replace(/\n/g, '<br>');
}

// ===========================
// Quick question shortcut
// ===========================
function askQuestion(question) {
    messageInput.value = question;
    sendMessage();
}

// ===========================
// Clear chat history
// ===========================
async function clearChat() {
    const sessionId = getSessionId();
    try {
        await fetch(`/api/chat/history/${sessionId}`, { method: 'DELETE' });
    } catch (e) { /* ignore */ }

    // Clear UI
    messagesArea.innerHTML = '';
    appendMessage('bot', 'Sohbet geçmişi temizlendi. Yeni bir soruyla başlayabilirsin! 🏁');
}

// ===========================
// UI Helpers
// ===========================
function setInputDisabled(disabled) {
    messageInput.disabled = disabled;
    sendBtn.disabled = disabled;
}

function showTyping(show) {
    if (typingIndicator) {
        typingIndicator.style.display = show ? 'flex' : 'none';
    }
}

function scrollToBottom() {
    requestAnimationFrame(() => {
        messagesArea.scrollTop = messagesArea.scrollHeight;
        if (typingIndicator) {
            typingIndicator.scrollIntoView({ behavior: 'smooth' });
        }
    });
}

function autoResize(el) {
    el.style.height = 'auto';
    el.style.height = Math.min(el.scrollHeight, 120) + 'px';
}

function handleKeyDown(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

// ===========================
// On page load: focus input
// ===========================
window.addEventListener('DOMContentLoaded', () => {
    if (messageInput) messageInput.focus();

    // Check for pre-set question (from driver cards etc.)
    const urlParams = new URLSearchParams(window.location.search);
    const preQuestion = urlParams.get('q');
    if (preQuestion) {
        messageInput.value = preQuestion;
        setTimeout(sendMessage, 500);
    }
});
